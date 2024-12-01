package taskmanager.dao;

import fr.iutrodez.taskmanager.dao.TaskDAO;
import fr.iutrodez.taskmanager.model.Task;
import fr.iutrodez.taskmanager.util.DBConnection;
import org.junit.jupiter.api.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TaskDAOTest {
    private TaskDAO taskDAO;

    public TaskDAOTest() throws SQLException {
        taskDAO = new TaskDAO();
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement()) {

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(getClass().getClassLoader().getResourceAsStream("init.sql"))
            );
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            stmt.execute(sb.toString());
        } catch (Exception e) {
            e.printStackTrace();
            fail("Échec de l'initialisation du schéma de la base de données.");
        }

    }

    @Test
    public void testAddTask() throws SQLException {
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute("TRUNCATE TABLE tasks RESTART IDENTITY");
        }
        Task task = new Task("Test Task");
        taskDAO.addTask(task);

        List<Task> tasks = taskDAO.getAllTasks();
        assertEquals(1, tasks.size());
        assertEquals("Test Task", tasks.get(0).getDescription());
    }

    @Test
    public void testDeleteTask() throws SQLException {
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute("TRUNCATE TABLE tasks RESTART IDENTITY");
        }
        Task task1 = new Task("Task 1");
        Task task2 = new Task("Task 2");
        taskDAO.addTask(task1);
        taskDAO.addTask(task2);

        List<Task> tasksBeforeDeletion = taskDAO.getAllTasks();
        assertEquals(2, tasksBeforeDeletion.size());

        int idToDelete = tasksBeforeDeletion.get(0).getId();
        taskDAO.deleteTask(idToDelete);

        List<Task> tasksAfterDeletion = taskDAO.getAllTasks();
        assertEquals(1, tasksAfterDeletion.size());
        assertEquals("Task 2", tasksAfterDeletion.get(0).getDescription());
    }

    @Test
    public void testGetAllTasks() throws SQLException {
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute("TRUNCATE TABLE tasks RESTART IDENTITY");
        }
        Task task1 = new Task("Task 1");
        Task task2 = new Task("Task 2");
        taskDAO.addTask(task1);
        taskDAO.addTask(task2);

        List<Task> tasks = taskDAO.getAllTasks();
        assertEquals(2, tasks.size());
        assertEquals("Task 1", tasks.get(0).getDescription());
        assertEquals("Task 2", tasks.get(1).getDescription());
    }
}
