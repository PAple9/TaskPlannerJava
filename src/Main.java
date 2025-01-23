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
        try{
        loadFromFile();

        Scanner scanner = new Scanner(System.in);
        System.out.println("Добро пожаловать в ваш список задач!");
        boolean flag = true;

        while(flag){
            System.out.println("Выберите действие:\n" +
                    "add - добавить задачу\n" +
                    "list - список задач\n" +
                    "done - отметить задачу как выполненную\n" +
                    "delete - удалить все выполненные задачи\n" +
                    "search - найти задачи по описанию\n" +
                    "exit - выход.");
            String todo = scanner.nextLine();

            switch (todo){
                case "exit":
                    System.out.println("Программа завершила работу, задачи сохранены!");
                    flag = false;
                    break;
                case "add":
                    System.out.println("Что вам нужно сделать еще?");
                    addTask(scanner.nextLine());
                    break;
                case "list":
                    listTasks();
                    break;
                case "done":
                    System.out.println("Введите номер задачи которую вы выполнили.");
                    markTaskAsDone(scanner.nextInt());
                    scanner.nextLine();
                    break;
                case "delete":
                    deleteCompletedTasks();
                    break;
                case "search":
                    System.out.println("Введите ключевое слово для поиска задачи.");
                    searchTasks(scanner.nextLine());
                    break;
                default:
                    System.out.println("Неизвестная команда: " + todo);
                    break;
                }
            }
        saveToFile();
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void loadFromFile() throws IOException{
        if(!file.exists()){
            throw new IOException("Файл с задачами не найден:(");
        }
        Scanner s = new Scanner(file);

        while (s.hasNext()) {
            String[] parseTask = s.nextLine().split(";");
            Task task = new Task();
            task.id = Integer.parseInt(parseTask[0]);
            task.status = Boolean.parseBoolean(parseTask[1]);
            task.description = parseTask[2];
            task.createdAt = LocalDateTime.parse(parseTask[3],formatter);
            list.add(task);
        }
        s.close();
    }

    public static void saveToFile(){
        try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
            for (Task task : list) {
                writer.println(task.id + ";" + task.status + ";" + task.description + ";" + task.createdAt.format(formatter));
            }
        } catch (IOException e) {
            System.out.println("Ошибка при записи в файл: " + e.getMessage());
        }
    }

    public static void addTask(String description){
        if(description.contains(";")){
            System.out.println("описание задачи не может содержать в себе символ ;");
        }
        else{
        Task task = new Task();
        task.description = description;
        task.status = false;
        task.createdAt = java.time.LocalDateTime.now();
        list.add(task);
        task.id = list.indexOf(task) + 1;
        }
    }


    public static void listTasks(){
        if(list.isEmpty()){
            System.out.println("В списке задач пусто :(");
        }
        else{
        for (Task task: list) {
            printTask(task);
            }
        }
    }

    public static void markTaskAsDone(int id){
        for(Task task: list){
            if(task.id == id){
                if(task.status){
                    System.out.println("задача которую вы хотите отметить уже выполнена");
                }
                else{
                    task.status = true;
                }
                System.out.println("Поздравляю с выполнением задачи: " + task.description + "!");
                return;
            }
        }
        System.out.println("Кажется вы ввели некорректный номер задачи :(");
    }

    public static void deleteCompletedTasks(){
        list.removeIf(task -> task.status);
        for( Task task: list){
            task.id = list.indexOf(task)+1;
        }
        System.out.println("Выполненные задачи успешно удалены! ");
    }

    public static void  searchTasks(String keyword){
        System.out.println("Все задачи, содержащие в себе " + keyword);
        for( Task task: list){
            if(task.description.contains(keyword)){
                printTask(task);
            }
        }
    }

    public static void printTask(Task task){
        String  status;
        if (task.status){
            status = "Выполненая задача:";
        }
        else{
            status = "Активная задача:";
        }

        String formatedTime = task.createdAt.format(formatter);
        System.out.println(task.id + " " + status + " " + task.description + " " + formatedTime);
        System.out.println("_______________________________________________________________");
    }
}