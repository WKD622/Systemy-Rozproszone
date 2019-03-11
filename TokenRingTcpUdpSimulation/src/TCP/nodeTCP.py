import socket
import sys
from threading import Thread
import time
import random
import struct

receiving_ip_address = None
receiving_port = None
new_node_port = None
i_have_token = None
receiving_socket = None
sending_socket = None
new_token_socket = None
new_node_socket = None
next_ip_address = None
next_port = None
id = None
token_to_delete = None
token_id = None
token_sender = False
token_to_set = None

NEW_SOCKET_ALERT_IP_ADDRESS = "230.1.1.2"
NEW_SOCKET_ALERT_PORT = 12346
LOGGER_IP_ADDRESS = "230.1.1.1"
LOGGER_PORT = 12345
BUFFER_SIZE = 40
NEW_NODE = "new_node"
NEW_NODE_ANSWER = "new_node_anwser"
TOKEN = "token"
MESSAGE = "message"
NEW_TOKEN = "new_token"
DELETE_TOKEN = "delete_token"


def read_parameters():
    global receiving_ip_address
    global receiving_port
    global new_node_port
    global i_have_token
    global receiving_socket
    global sending_socket
    global new_node_socket
    global next_ip_address
    global next_port
    global id

    id = sys.argv[1]
    receiving_ip_address = sys.argv[2]
    receiving_port = int(sys.argv[3])
    new_node_port = int(sys.argv[4])
    next_ip_address = sys.argv[5]
    next_port = int(sys.argv[6])
    if len(sys.argv) > 7:
        i_have_token = True
    else:
        i_have_token = False

    print("MY IP: " + receiving_ip_address + " | RECEIVING PORT: " + str(receiving_port))
    print("MY IP: " + receiving_ip_address + " | NEW NODE PORT: " + str(new_node_port))
    print("NEXT IP: " + next_ip_address + " | NEXT PORT: " + str(next_port) + "\n")


def create_message(type, destination_ip_address, message):
    return type + " " + destination_ip_address + " " + message;


def check_if_message_for_me(message):
    if message.split()[1] == receiving_ip_address:
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
    global next_ip_address
    global next_port
    global token_id
    buff_1 = []
    buff_2 = []
    while True:
        connection, address = receiving_socket.accept()
        buff_1 = connection.recv(BUFFER_SIZE)
        type = get_message_type(str(buff_1, 'utf-8'))
        if type == MESSAGE:
            if check_if_message_for_me(str(buff_1, 'utf-8')):
                print("RECEIVED MESSAGE: " + str(buff_1, 'utf-8').split(" ")[2])
                print("TYPE: DESTINATION IP + ONE WORD MESSAGE:")
            else:
                print("TRANSFERING A MESSAGE TO THE NEXT NODE")
                message_counter = get_message_counter(buff_1)
                if message_counter > 0:
                    buff_1 = decrement_message_counter(buff_1)
                    sending_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
                    sending_socket.connect((next_ip_address, next_port))
                    sending_socket.send(buff_1)
                    sending_socket.close()
                else:
                    print("MESSAGE DELETED, THIS IP DOESN'T EXIST IN NETWORK")
        elif type == TOKEN:
            if token_id is None:
                token_id = int(str(buff_1, 'utf-8').split(" ")[1])
            Thread(target=tokening).start()
        elif type == NEW_NODE:
            buff_2 = NEW_NODE_ANSWER + " " + next_ip_address + " " + str(next_port)
            next_ip_address = str(buff_1, 'utf-8').split(" ")[1]
            next_port = int(str(buff_1, 'utf-8').split(" ")[2])
            connection.send(bytes(buff_2, 'utf-8'))


def send_new_message():
    global sending_socket
    global i_have_token
    while True:
        print("TYPE: DESTINATION IP + ONE WORD MESSAGE:")
        input_ = input()
        destination_ip_address = input_.split(" ")[0]
        message = input_.split(" ")[1]
        buff = MESSAGE + " " + destination_ip_address + " " + message + " " + str(10)
        while i_have_token is False:
            print("WAITING FOR TOKEN")
            time.sleep(1)
        sending_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        sending_socket.connect((next_ip_address, next_port))
        sending_socket.send(bytes(buff, 'utf-8'))
        sending_socket.close()
        i_have_token = False


def tokening():
    global i_have_token
    global sending_socket
    global token_sender
    global token_to_delete
    global token_id
    global token_to_set

    i_have_token = True
    time.sleep(1)
    i_have_token = False
    buff = TOKEN + " " + str(token_id)
    if token_sender == True and token_to_delete == token_id:
        print("TOKEN " + str(token_id) + " WON'T BE SENT FURTHER")
        token_sender = False
        token_to_delete = None
        token_id = token_to_set
        token_to_set = None
        token_sender = False
        attach_new_node()
        print("TYPE: DESTINATION IP + ONE WORD MESSAGE:")
    else:
        sending_socket.close()
        sending_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        sending_socket.connect((next_ip_address, next_port))
        sending_socket.send(bytes(buff, 'utf-8'))

        # LOGGING
        sock = socket.socket(socket.AF_INET, socket.SOCK_DGRAM, socket.IPPROTO_UDP)
        sock.setsockopt(socket.IPPROTO_IP, socket.IP_MULTICAST_TTL, 32)
        sock.sendto(bytes(id + " GOT TOKEN", 'utf-8'), (LOGGER_IP_ADDRESS, LOGGER_PORT))


def attach_new_node():
    global sending_socket
    global next_ip_address
    global next_port
    buff = NEW_NODE + " " + receiving_ip_address + " " + str(receiving_port)
    sending_socket.connect((next_ip_address, next_port))
    sending_socket.send(bytes(buff, 'utf-8'))
    data = sending_socket.recv(BUFFER_SIZE)
    next_ip_address = str(data, 'utf-8').split(" ")[1]
    next_port = int(str(data, 'utf-8').split(" ")[2])
    print("I'M IN NETWORK!")
    sending_socket.close()


def send_new_token_information():
    print("NEW TOKEN " + str(token_id) + " IN NETWORK")
    sock = socket.socket(socket.AF_INET, socket.SOCK_DGRAM, socket.IPPROTO_UDP)
    sock.setsockopt(socket.IPPROTO_IP, socket.IP_MULTICAST_TTL, 2)
    sock.sendto(bytes(NEW_TOKEN + " " + str(token_id), 'utf-8'), (NEW_SOCKET_ALERT_IP_ADDRESS, NEW_SOCKET_ALERT_PORT))


def listen_for_new_token_in_network_alert():
    global token_to_delete
    global token_id
    global token_to_set
    buff = []
    while True:
        buff = new_token_socket.recv(30)
        if str(buff, 'utf-8').split(" ")[0] == NEW_TOKEN:
            if token_sender == True:
                new_token = int(str(buff, 'utf-8').split(" ")[1])
                if new_token != token_id:
                    token_to_delete = new_token
                    print("TOKEN " + str(token_to_delete) + " MUST BE DELETED")
                    sock = socket.socket(socket.AF_INET, socket.SOCK_DGRAM, socket.IPPROTO_UDP)
                    sock.setsockopt(socket.IPPROTO_IP, socket.IP_MULTICAST_TTL, 2)
                    sock.sendto(
                        bytes(DELETE_TOKEN + " " + str(token_to_delete) + " " + str(token_id), 'utf-8'),
                        (NEW_SOCKET_ALERT_IP_ADDRESS, NEW_SOCKET_ALERT_PORT))
        if str(buff, 'utf-8').split(" ")[0] == DELETE_TOKEN:
            if token_sender == True:
                if token_id == int(str(buff, 'utf-8').split(" ")[1]):
                    token_to_delete = token_id
                    token_to_set = int(str(buff, 'utf-8').split(" ")[2])


def main():
    global receiving_socket
    global sending_socket
    global new_node_socket
    global i_have_token
    global token_id
    global new_token_socket
    global token_sender
    global receiving_ip_address

    read_parameters()

    receiving_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    receiving_socket.bind((receiving_ip_address, receiving_port))
    receiving_socket.listen(1)

    sending_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

    new_node_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    new_node_socket.bind((receiving_ip_address, new_node_port))
    new_node_socket.listen(1)

    new_token_socket = socket.socket(socket.AF_INET, socket.SOCK_DGRAM, socket.IPPROTO_UDP)
    new_token_socket.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
    new_token_socket.bind((NEW_SOCKET_ALERT_IP_ADDRESS, NEW_SOCKET_ALERT_PORT))
    mreq = struct.pack("4sl", socket.inet_aton(NEW_SOCKET_ALERT_IP_ADDRESS), socket.INADDR_ANY)
    new_token_socket.setsockopt(socket.IPPROTO_IP, socket.IP_ADD_MEMBERSHIP, mreq)

    if i_have_token == True:
        token_sender = True
        token_id = random.randint(100, 999)
        Thread(target=tokening).start()
        send_new_token_information()
    else:
        attach_new_node()

    Thread(target=listen_for_new_token_in_network_alert).start()
    Thread(target=receiving).start()
    Thread(target=send_new_message).start()


main()
