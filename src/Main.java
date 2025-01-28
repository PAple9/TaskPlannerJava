import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    static File file = new File("tasks.txt");
    static ArrayList<Task> list = new ArrayList<>();
    static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static void main(String[] args) {
        try {
            loadFromFile();

            Scanner scanner = new Scanner(System.in);
            System.out.println("Добро пожаловать в ваш список задач!");
            boolean flag = true;

            while (flag) {
                System.out.println("Выберите действие:\n" +
                        "add - добавить задачу\n" +
                        "list - список задач\n" +
                        "done - отметить задачу как выполненную\n" +
                        "delete - удалить все выполненные задачи\n" +
                        "search - найти задачи по описанию\n" +
                        "exit - выход.");
                String todo = scanner.nextLine();
                String[] command = todo.split(" ");
                switch (command[0]) {
                    case "exit":
                        System.out.println("Программа завершила работу, задачи сохранены!");
                        flag = false;
                        break;
                    case "add":
                        if(command.length < 2){
                            System.out.println("Вводи команду и данные в одну строку!");
                        }
                        else{
                            addTask(todo.substring(4));
                        }
                        break;
                    case "list":
                        listTasks();
                        break;
                    case "done":
                        if(command.length < 2){
                            System.out.println("Вводи команду и данные в одну строку!");
                        }
                        markTaskAsDone(Integer.parseInt(command[1]));
                        break;
                    case "delete":
                        deleteCompletedTasks();
                        break;
                    case "search":
                        if(command.length < 2){
                            System.out.println("Вводи команду и данные в одну строку!");
                        }
                        searchTasks(todo.substring(7));
                        break;
                    default:
                        System.out.println("Неизвестная команда: " + todo);
                        break;
                }
            }
            saveToFile();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void loadFromFile() throws IOException {
        if (!file.exists()) {
            throw new IOException("Файл с задачами не найден:(");
        }
        Scanner s = new Scanner(file);

        while (s.hasNext()) {
            String[] parseTask = s.nextLine().split(";");
            Integer id = Integer.parseInt(parseTask[0]);
            boolean status = Boolean.parseBoolean(parseTask[1]);
            String description = parseTask[2];
            LocalDateTime createdAt = LocalDateTime.parse(parseTask[3], formatter);
            Task task = new Task(id, description, status, createdAt);
            list.add(task);
        }
        s.close();
    }

    public static void saveToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
            for (Task task : list) {
                writer.println(task.getId() + ";" + task.getStatus() + ";" + task.getDescription() + ";" + task.getTime().format(formatter));
            }
        } catch (IOException e) {
            System.out.println("Ошибка при записи в файл: " + e.getMessage());
        }
    }

    public static void addTask(String description) {
        if (description.contains(";")) {
            System.out.println("описание задачи не может содержать в себе символ ;");
        } else {
            Task task = new Task(list.size() + 1, description, false, java.time.LocalDateTime.now());
            list.add(task);
        }
    }


    public static void listTasks() {
        if (list.isEmpty()) {
            System.out.println("В списке задач пусто :(");
        } else {
            for (Task task : list) {
                printTask(task);
            }
        }
    }

    public static void markTaskAsDone(int id) {
        for (Task task : list) {
            if (task.getId() == id) {
                if (task.getStatus()) {
                    System.out.println("задача которую вы хотите отметить уже выполнена");
                } else {
                    task.setStatus(true);
                }
                System.out.println("Поздравляю с выполнением задачи: " + task.getDescription() + "!");
                return;
            }
        }
        System.out.println("Кажется вы ввели некорректный номер задачи :(");
    }

    public static void deleteCompletedTasks() {
        list.removeIf(Task::getStatus);
        for (Task task : list) {
            task.setId(list.indexOf(task) + 1);
        }
        System.out.println("Выполненные задачи успешно удалены! ");
    }

    public static void searchTasks(String keyword) {
        System.out.println("Все задачи, содержащие в себе " + keyword);
        for (Task task : list) {
            if (task.getDescription().contains(keyword)) {
                printTask(task);
            }
        }
    }

    public static void printTask(Task task) {
        String status;
        if (task.getStatus()) {
            status = "Выполненая задача:";
        } else {
            status = "Активная задача:";
        }

        String formatedTime = task.getTime().format(formatter);
        System.out.println(task.getId() + " " + status + " " + task.getDescription() + " " + formatedTime);
        System.out.println("_______________________________________________________________");
    }
}