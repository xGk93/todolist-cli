package fr.iutrodez.taskmanager;

import fr.iutrodez.taskmanager.dao.TaskDAO;
import fr.iutrodez.taskmanager.model.Task;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class MainApplication {

    private static TaskDAO taskDAO = new TaskDAO();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("=== Gestionnaire de Tâches ===");
        boolean exit = false;
        while (!exit) {
            printMenu();
            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    addTask();
                    break;
                case "2":
                    deleteTask();
                    break;
                case "3":
                    listTasks();
                    break;
                case "4":
                    exit = true;
                    System.out.println("Au revoir !");
                    break;
                default:
                    System.out.println("Choix invalide. Veuillez réessayer.");
            }
        }
    }

    private static void printMenu() {
        System.out.println("\nSélectionnez une option:");
        System.out.println("1. Ajouter une tâche");
        System.out.println("2. Supprimer une tâche");
        System.out.println("3. Lister toutes les tâches");
        System.out.println("4. Quitter");
        System.out.print("Votre choix: ");
    }

    private static void addTask() {
        System.out.print("Entrez la description de la tâche: ");
        String description = scanner.nextLine();
        Task task = new Task(description);
        try {
            taskDAO.addTask(task);
            System.out.println("Tâche ajoutée avec succès.");
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout de la tâche: " + e.getMessage());
        }
    }

    private static void deleteTask() {
        System.out.print("Entrez l'ID de la tâche à supprimer: ");
        String input = scanner.nextLine();
        try {
            int id = Integer.parseInt(input);
            taskDAO.deleteTask(id);
            System.out.println("Tâche supprimée avec succès.");
        } catch (NumberFormatException e) {
            System.err.println("ID invalide. Veuillez entrer un nombre entier.");
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression de la tâche: " + e.getMessage());
        }
    }

    private static void listTasks() {
        try {
            List<Task> tasks = taskDAO.getAllTasks();
            if (tasks.isEmpty()) {
                System.out.println("Aucune tâche trouvée.");
            } else {
                System.out.println("\nListe des tâches:");
                for (Task task : tasks) {
                    System.out.println(task.getId() + ": " + task.getDescription() + " (Créée le " + task.getCreatedAt() + ")");
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des tâches: " + e.getMessage());
        }
    }
}
