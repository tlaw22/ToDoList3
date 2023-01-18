module com.tlaw.todolist3 {
      requires javafx.controls;
      requires javafx.fxml;


      opens com.tlaw.todolist3 to javafx.fxml;
      exports com.tlaw.todolist3;
      exports com.tlaw.todolist3.DataModel;
      opens com.tlaw.todolist3.DataModel to javafx.fxml;
}