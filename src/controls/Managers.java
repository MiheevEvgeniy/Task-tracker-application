package controls;

public class Managers {
    TaskManager getDefault(){
        return new InMemoryTaskManager();
    }

     HistoryManager getDefaultHistory(){
        return new InMemoryHistoryManager();
    }
}
