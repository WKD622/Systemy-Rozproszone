import java.io.*;
import java.util.Arrays;

public class DbOperations {
    private String DATABASE_PATH = "database";
    private String fileSeparator = System.getProperty("file.separator");
    private File file;

    public void createLoadDB(String filename) {
        this.file = new File(this.DATABASE_PATH + this.fileSeparator + filename);
    }

    public boolean checkIfDbExists() {
        return this.file.exists();
    }

    public String searchForTitle(String title) {
        String information = this.getRecord(title);
        if (information.equals("")) {
            return "No such title";
        }
        return this.getTitle(information) + " " + this.getPrice(information) + " " + this.getAvailability(information);
    }

    public boolean checkIfExists(String title){
        String information = this.getRecord(title);
        if (information.equals("")) {
            return false;
        }
        return true;
    }

    public boolean checkIfAvailable(String title) {
        String information = this.getRecord(title);
        if (information.equals("")) {
            return false;
        }
        return this.getAvailability(information).equals("available");
    }

    public void addRecord(String title, int price) {
        Writer output = null;
        try {
            output = new BufferedWriter(new FileWriter(file.getAbsolutePath(), true));  //clears file every time
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            output.append(title + "##" + String.valueOf(price) + "##available\n");
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getPrice(String information) {
        return information.split("##")[1];
    }

    private String getTitle(String information) {
        System.out.println("getTitle " + information);
        System.out.println(Arrays.toString(information.split("##")));
        return information.split("##")[0];
    }

    private String getAvailability(String information) {
        return information.split("##")[2];
    }

    private String getRecord(String title) {
        System.out.println(file.exists());
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(file.getAbsolutePath()));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            String line = br.readLine();

            while (line != null) {
                if (line.contains(title)) {
                    return line;
                }
                line = br.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    public void returnBook(String title) {
        String record = this.getRecord(title);
        System.out.println(record);
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(this.file.getAbsolutePath()));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        File tmp = new File(this.DATABASE_PATH + this.fileSeparator + "tmp");
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriter(tmp));
        } catch (IOException e) {
            e.printStackTrace();
        }
        String removeID = title;
        String currentLine = null;
        while (true) {
            try {
                if (!((currentLine = br.readLine()) != null)) break;
            } catch (IOException e) {
                e.printStackTrace();
            }
            String trimmedLine = currentLine.trim();
            if (trimmedLine.contains(removeID)) {
                currentLine = null;
            }
            try {
                if (currentLine != null) {
                    bw.write(currentLine + System.getProperty("line.separator"));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        try {
            bw.append(this.getTitle(record) + "##" + this.getPrice(record) + "##available");
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            bw.close();
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        boolean delete = this.file.delete();
        boolean b = tmp.renameTo(this.file);
    }

    public void borrowBook(String title) {
        String record = this.getRecord(title);
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(this.file.getAbsolutePath()));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        File tmp = new File(this.DATABASE_PATH + this.fileSeparator + "tmp");
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriter(tmp));
        } catch (IOException e) {
            e.printStackTrace();
        }
        String removeID = title;
        String currentLine = null;
        while (true) {
            try {
                if (!((currentLine = br.readLine()) != null)) break;
            } catch (IOException e) {
                e.printStackTrace();
            }
            String trimmedLine = currentLine.trim();
            if (trimmedLine.contains(removeID)) {
                currentLine = null;
            }
            try {
                if (currentLine != null) {
                    bw.write(currentLine + System.getProperty("line.separator"));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        try {
            bw.append(this.getTitle(record) + "##" + this.getPrice(record) + "##notavailable");
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            bw.close();
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        boolean delete = this.file.delete();
        boolean b = tmp.renameTo(this.file);
    }

    public void addOrder(String title) {
        File orders = new File(this.DATABASE_PATH + this.fileSeparator + "orders");
        Writer output = null;
        try {
            output = new BufferedWriter(new FileWriter(orders.getAbsolutePath(), true));  //clears file every time
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            output.append(title + "\n");
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteOrder(String title) {
        File orders = new File(this.DATABASE_PATH + this.fileSeparator + "orders");
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(orders.getAbsolutePath()));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        File tmp = new File(this.DATABASE_PATH + this.fileSeparator + "orders_tmp");
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriter(tmp));
        } catch (IOException e) {
            e.printStackTrace();
        }
        String removeID = title;
        String currentLine = null;
        while (true) {
            try {
                if (!((currentLine = br.readLine()) != null)) break;
            } catch (IOException e) {
                e.printStackTrace();
            }
            String trimmedLine = currentLine.trim();
            if (trimmedLine.contains(removeID)) {
                currentLine = null;
            }
            try {
                if (currentLine != null) {
                    bw.write(currentLine + System.getProperty("line.separator"));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        try {
            bw.close();
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        boolean delete = orders.delete();
        boolean b = tmp.renameTo(orders);
    }

}
