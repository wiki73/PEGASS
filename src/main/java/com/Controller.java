package com;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.*;
import javafx.scene.Scene;
import javafx.scene.image.WritableImage;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class Controller {

    @FXML
    private ListView<String> ShortList1;
    @FXML
    private ListView<String> ShortList2;
    @FXML
    private ListView<String> ShortList3;

    @FXML
    private TextField taskInputField;


    @FXML
    private void openGoals(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Goals.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Цели");
            stage.setScene(new Scene(root));

            // Запуск в полноэкранном режиме
            stage.setFullScreen(true);

            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void openBattle(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Battle.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Драка");
            stage.setScene(new Scene(root));

            // Запуск в полноэкранном режиме
            stage.setFullScreen(true);

            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void openDiary(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Diary.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Дневник");
            stage.setScene(new Scene(root));

            // Запуск в полноэкранном режиме
            stage.setFullScreen(true);

            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // Инициализация контроллера
//    @FXML
//    public void initialize() {
//        // Настраиваем cellFactory для применения стилей к каждой ячейке
//        ShortList1.setCellFactory(lv -> new ListCell<String>() {
//            @Override
//            protected void updateItem(String item, boolean empty) {
//                super.updateItem(item, empty);
//
//                if (empty || item == null) {
//                    setText(null);
//                    setGraphic(null);
//                    setStyle(""); // Сбрасываем стиль для пустых ячеек
//                } else {
//                    setText(item);
//                    // Применяем стиль с красным текстом
//                    setStyle("-fx-text-fill: white; -fx-font-size: 14px;");
//                }
//            }
//        });
//
//    }
    public void initialize() {
        // Заполняем тестовыми данными
        ShortList1.setItems(FXCollections.observableArrayList("Элемент 1", "Элемент 2", "Элемент 3"));
        ShortList2.setItems(FXCollections.observableArrayList("Элемент A", "Элемент B"));
        ShortList3.setItems(FXCollections.observableArrayList("Элемент X", "Элемент Y"));
        // Настраиваем drag&drop для всех комбинаций
        enableDragAndDrop(ShortList1, ShortList2);
        enableDragAndDrop(ShortList1, ShortList3);

        enableDragAndDrop(ShortList2, ShortList1);
        enableDragAndDrop(ShortList2, ShortList3);

        enableDragAndDrop(ShortList3, ShortList1);
        enableDragAndDrop(ShortList3, ShortList2);
    }

    private void enableDragAndDrop(ListView<String> source, ListView<String> target) {
        // Начало перетаскивания
        source.setOnDragDetected(event -> {
            String selected = source.getSelectionModel().getSelectedItem();
            if (selected == null) {
                return;
            }

            Dragboard db = source.startDragAndDrop(TransferMode.MOVE);
            ClipboardContent content = new ClipboardContent();
            content.putString(selected);
            db.setContent(content);

            // ---- создаём визуал для drag-preview ----
            Label dragLabel = new Label(selected);
            dragLabel.setPrefSize(200,30);
            // стиль можно настроить под вид твоих ячеек
            dragLabel.setStyle(
                    "-fx-background-color: transparent; " +  // фон
                            "-fx-border-color: gray; " +           // рамка
                            "-fx-border-radius: 10; " +            // закругление рамки
                            "-fx-background-radius: 10; " +        // закругление фона
                            "-fx-font-size: 14px; " +
                            "-fx-alignment: center-left; " +
                            "-fx-padding: 5px;"
            );
            dragLabel.applyCss();
            dragLabel.layout();

            StackPane wrapper = new StackPane(dragLabel);
            wrapper.setPadding(new Insets(2));
            wrapper.setStyle(
                    "-fx-background-color: lightblue; " +
                            "-fx-background-radius: 10; " +
                            "-fx-border-radius: 10; " +
                            "-fx-border-color: gray;"
            );
            // временная (невидимая) сцена — чтобы CSS/метрики применились
            Scene tmpScene = new Scene(wrapper);
            // принудительно применяем CSS и layout
            wrapper.applyCss();
            wrapper.layout();;

            // делаем снимок
            WritableImage snapshot = wrapper.snapshot(new SnapshotParameters(), null);
            // проверяем размер — если всё ок, ставим preview
            if (snapshot != null && snapshot.getWidth() > 0 && snapshot.getHeight() > 0) {
                db.setDragView(snapshot, snapshot.getWidth(), snapshot.getHeight());
            }
            // ------------------------------------------

            event.consume();
        });
        source.setOnMouseReleased(event -> {
            source.getSelectionModel().clearSelection();
        });
        // Наведение на цель
        target.setOnDragOver(event -> {
            if (event.getGestureSource() != target && event.getDragboard().hasString()) {
                event.acceptTransferModes(TransferMode.MOVE);
                target.setStyle("-fx-border-color: green; -fx-border-width: 2px;"); // подсветка
            }
            event.consume();
        });

        // Убираем подсветку, если мышь ушла
        target.setOnDragExited(event -> {
            target.setStyle(""); // сброс стиля
        });

        // Бросание в цель
        target.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;
            if (db.hasString()) {
                String item = db.getString();
                target.getItems().add(item); // добавляем в целевой список

                // получаем настоящий источник
                Object gestureSource = event.getGestureSource();
                if (gestureSource instanceof ListView) {
                    ListView<?> realSource = (ListView<?>) gestureSource;
                    realSource.getItems().remove(item);
                }

                success = true;
            }
            event.setDropCompleted(success);
            target.setStyle(""); // убираем подсветку
            event.consume();
        });
    }


    @FXML
    private void handleAddTask(){
        String task = taskInputField.getText().trim();
        if (!task.isEmpty()) {
            ShortList1.getItems().add(task);
            taskInputField.clear();
        }
    }
}
