package com;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.stage.Stage;

public class DiaryController {
    @FXML
    private void goBack(ActionEvent event) {
        // Закрыть текущее окно (Goals.fxml)
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();

        // Сделать главное окно полноэкранным
        for (Stage s : Stage.getWindows().stream().filter(w -> w instanceof Stage).map(w -> (Stage) w).toList()) {
            if (s.isShowing() && s.getTitle().equals("Kight Game")) {
                s.setFullScreen(true);
            }
        }
    }
}
