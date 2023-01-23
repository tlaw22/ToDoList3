package com.tlaw.todolist3;


import com.tlaw.todolist3.DataModel.TodoData;
import com.tlaw.todolist3.DataModel.TodoItem;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.time.LocalDate;


public class DialogController {

      @FXML
      private TextField shortDesc;

      @FXML
      private TextArea detailsArea;

      @FXML
      private DatePicker deadlinePicker;

      public TodoItem processResults() {
            String shortDescription = shortDesc.getText().trim();
            String details = detailsArea.getText().trim();
            LocalDate deadlineValue = deadlinePicker.getValue();

            TodoItem newItem = new TodoItem(shortDescription, details, deadlineValue);
            TodoData.getInstance().addTodoItem(newItem);
            return newItem;
      }
}
