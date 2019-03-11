import socket
import sys
from threading import Thread
import time

my_ip_address = None
my_port = None
i_have_token = None
my_socket = None
next_ip_address = None
next_port = None
id = None
message_to_send = None
logger_ip_address = "230.1.1.1"
logger_port = 12345

NEW_NODE = "new_node"
NEW_NODE_ANSWER = "new_node_anwser"
TOKEN = "token"
MESSAGE = "message"


def read_parameters():
    global my_ip_address
    global my_port
    global i_have_token
    global my_socket
    global next_ip_address
    global next_port
    global id

    id = sys.argv[1]
    my_ip_address = sys.argv[2]
    my_port = int(sys.argv[3])
    next_ip_address = sys.argv[4]
    next_port = int(sys.argv[5])
    if len(sys.argv) > 6:
        i_have_token = True
    else:
        i_have_token = False

    print(my_ip_address + " " + str(my_port))


def create_message(type, destination_ip_address, message):
    return type + " " + destination_ip_address + " " + message;


def check_if_message_for_me(message):
    if message.split()[1] == my_ip_address:
        return True
    else:
        return False


def get_message_type(message):
    return message.split(" ")[0]


def get_message_counter(buff):
    message = str(buff, 'utf-8')
    return int(message.split(" ")[3])


def decrement_message_counter(buff):
    message = str(buff, 'utf-8')
    message_as_list = message.split(" ")
    destination_ip_address = message_as_list[1]
    content = message_as_list[2]
    counter = int(message_as_list[3])
    counter = counter - 1
    return bytes(MESSAGE + " " + destination_ip_address + " " + content + " " + str(counter), 'utf-8')


def receiving():
    global my_socket
    global next_ip_address
    global i_have_token
    global next_port
    buff = []
    while True:
        buff, address = my_socket.recvfrom(1024)
        type = get_message_type(str(buff, 'utf-8'))
        if type == MESSAGE:
            if check_if_message_for_me(str(buff, 'utf-8')):
                print("RECEIVED MESSAGE: " + str(buff, 'utf-8').split(" ")[2])
                print("TYPE: DESTINATION IP + ONE WORD MESSAGE:")
                Thread(target=tokening).start()
            else:
                print("TRANSFERING A MESSAGE TO THE NEXT NODE")
                message_counter = get_message_counter(buff)
                if message_counter > 0:
                    buff = decrement_message_counter(buff)
                    my_socket.sendto(buff, (next_ip_address, next_port))
                else:
                    print("MESSAGE DELETED, THIS IP DOESN'T EXIST IN NETWORK")
        elif type == TOKEN:
            Thread(target=tokening).start()
        elif type == NEW_NODE:
            buff = NEW_NODE_ANSWER + " " + next_ip_address + " " + str(next_port)
            my_socket.sendto(bytes(buff, 'utf-8'), address)
            next_ip_address = address[0]
            next_port = address[1]
        else:
            next_ip_address = str(buff, 'utf-8').split(" ")[1]
            next_port = int(str(buff, 'utf-8').split(" ")[2])
            print("I'M IN NETWORK!")


def send_new_message():
    global my_socket
    global i_have_token
    while True:
        print("TYPE: DESTINATION IP + ONE WORD MESSAGE:")
        input_ = input()
        destination_ip_address = input_.split(" ")[0]
        message = input_.split(" ")[1]
        # print("Your message: " + destination_ip_address + " " + message)
        buff = MESSAGE + " " + destination_ip_address + " " + message + " " + str(10)
        while i_have_token is False:
            print("WAITING FOR TOKEN")
            time.sleep(1)
        my_socket.sendto(bytes(buff, 'utf-8'), (next_ip_address, next_port))
        i_have_token = False


def tokening():
    global i_have_token
    i_have_token = True
    time.sleep(1)
    i_have_token = False
    buff = TOKEN
    my_socket.sendto(bytes(buff, 'utf-8'), (next_ip_address, next_port))
    sock = socket.socket(socket.AF_INET, socket.SOCK_DGRAM, socket.IPPROTO_UDP)
    sock.setsockopt(socket.IPPROTO_IP, socket.IP_MULTICAST_TTL, 32)
    sock.sendto(bytes(id + " GOT TOKEN", 'utf-8'), (logger_ip_address, logger_port))


def attach_new_node():
    buff = NEW_NODE + " " + my_ip_address + " " + str(my_port)
    my_socket.sendto(bytes(buff, 'utf-8'), (next_ip_address, next_port))


def main():
    global my_socket
    read_parameters()
    my_socket = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
    my_socket.bind((my_ip_address, my_port))
    if i_have_token == True:
        Thread(target=tokening).start()
    else:
        attach_new_node()

    Thread(target=receiving).start()
    Thread(target=send_new_message).start()


main()
