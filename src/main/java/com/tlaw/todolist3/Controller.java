package com.tlaw.todolist3;

import com.tlaw.todolist3.DataModel.TodoData;
import com.tlaw.todolist3.DataModel.TodoItem;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.util.Callback;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

public class Controller {

      private List<TodoItem> todoItems;

      @FXML
      private ListView<TodoItem> todoListView;

      @FXML
      private TextArea itemDetailsTextArea;

      @FXML
      private Label deadlineLabel;

      @FXML
      private BorderPane mainBorderPane;
      @FXML
      private ContextMenu listContextMenu;

      public void initialize() {
      listContextMenu = new ContextMenu();
      MenuItem deleteMenuItem = new MenuItem("Delete");

      deleteMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                  TodoItem item = todoListView.getSelectionModel().getSelectedItem();
                  deleteItem(item);

            }
      });
listContextMenu.getItems().addAll(deleteMenuItem);


            todoListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TodoItem>() {
                  @Override
                  public void changed(ObservableValue<? extends TodoItem> observable, TodoItem oldValue, TodoItem newValue) {
                        if(newValue != null) {
                              TodoItem item = todoListView.getSelectionModel().getSelectedItem();
                              itemDetailsTextArea.setText(item.getDetails());
                              DateTimeFormatter df = DateTimeFormatter.ofPattern("MMMM d, yyyy"); // "d M yy");
                              deadlineLabel.setText(df.format(item.getDeadline()));
                        }
                  }
            });

            todoListView.setItems(TodoData.getInstance().getTodoItems());
            todoListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
            todoListView.getSelectionModel().selectFirst();
            todoListView.setCellFactory(new Callback<ListView<TodoItem>, ListCell<TodoItem>>() {
                  @Override
                  public ListCell<TodoItem> call(ListView<TodoItem> param) {
                        ListCell<TodoItem> cell = new ListCell<TodoItem>() {

                              @Override
                              protected void updateItem(TodoItem item, boolean empty) {
                                    super.updateItem(item, empty);
                                    if(empty) {
                                          setText(null);
                                    } else {
                                          setText(item.getShortDescription());
                                          if(item.getDeadline().isBefore(LocalDate.now().plusDays(1))) {
                                                setTextFill(Color.RED);
                                          } else if(item.getDeadline().equals(LocalDate.now().plusDays(1))) {
                                                setTextFill(Color.BROWN);
                                          }
                                    }
                              }
                        };
                        cell.emptyProperty().addListener(
                              (obs, wasEmpty, isNowEmpty) -> {
                                    if(isNowEmpty){
                                          cell.setContextMenu(null);
                                    } else {
                                          cell.setContextMenu(listContextMenu);
                                    }
                              }
                        );
                        return cell;
                  }
            });
      }

      @FXML
      public void showNewItemDialog() {
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.initOwner(mainBorderPane.getScene().getWindow());
            dialog.setTitle("Add to database - Dialog");
            dialog.setHeaderText("Add a new log entry");
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("todoitemDialog.fxml"));
            try {
                  dialog.getDialogPane().setContent(fxmlLoader.load());

            } catch(IOException e) {
                  System.out.println("Couldn't load the dialog");
                  e.printStackTrace();
                  return;
            } // Add dialog buttons
            dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
            dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
            Optional<ButtonType> result = dialog.showAndWait(); // process results
            if(result.isPresent() && result.get() == ButtonType.OK){
                  System.out.println("Ok pressed");
                  DialogController controller = fxmlLoader.getController();
                  TodoItem newItem = controller.processResults();
                  todoListView.getSelectionModel().select(newItem);
                  System.out.println("Processed results");
            } else {
                  System.out.println("Cancel pressed");
            }

      }
      @FXML
      public void handleKeyPressed(KeyEvent keyEvent) {
            TodoItem selectedItem = todoListView.getSelectionModel().getSelectedItem();
            if(selectedItem != null) {
                  if(keyEvent.getCode().equals(KeyCode.DELETE)) {
                        deleteItem(selectedItem);
                  }
            }
      }


      @FXML
      public void handleClickListView() {
            TodoItem item = todoListView.getSelectionModel().getSelectedItem();
//            itemDetailsTextArea.setText(item.getDetails());
//            deadlineLabel.setText(item.getDeadline().toString());
      }
      public void deleteItem(TodoItem item){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete this item from the DATABASE?");
            alert.setHeaderText("Delete itemL" + item.getShortDescription());
            alert.setContentText("Are you sure?");
            Optional<ButtonType> result = alert.showAndWait();
            if(result.isPresent() && (result.get() == ButtonType.OK)){
                  TodoData.getInstance().deleteTodoItem(item);
            }
      }
}
