package com;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class MainApplication extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/design.fxml"));

        // Получаем видимые границы экрана
        Rectangle2D visualBounds = Screen.getPrimary().getVisualBounds();

        // Создаем сцену
        Scene scene = new Scene(root, 800, 600);

        primaryStage.setTitle("Kight Game");
        primaryStage.setScene(scene);

        // Центрируем окно при запуске
        primaryStage.centerOnScreen();

        // **ОСНОВНОЙ КОД: Обработка кнопки максимизации**
        setupMaximizeButtonBehavior(primaryStage);
        primaryStage.maximizedProperty().addListener((obs, wasMax, isNowMax) -> {
            if (isNowMax) {
                primaryStage.setMaximized(false);      // сбросить обычное разворачивание
                primaryStage.setFullScreen(true);      // включить настоящий fullscreen
            }
        });

        primaryStage.show();
    }

    /**
     * Настраиваем поведение кнопки максимизации
     */
    private void setupMaximizeButtonBehavior(Stage stage) {
        // Слушатель изменения состояния окна (максимизация/восстановление)
        stage.maximizedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                // Когда пользователь нажимает кнопку максимизации - включаем полноэкранный режим
                Platform.runLater(() -> {
                    stage.setMaximized(false); // Отключаем обычную максимизацию
                    stage.setFullScreen(true); // Включаем полноэкранный режим
                });
            }
        });

        // Обработка выхода из полноэкранного режима
        stage.fullScreenProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                // При выходе из полноэкранного режима корректируем позицию
                Platform.runLater(() -> {
                    correctWindowPosition(stage);
                });
            }
        });

        // Двойной клик по заголовку окна тоже будет включать полноэкранный режим
        stage.maximizedProperty().addListener((observable, oldValue, newValue) -> {
            // JavaFX автоматически обрабатывает двойной клик по заголовку как maximize
        });
    }

    /**
     * Корректируем позицию окна после выхода из полноэкранного режима
     */
    private void correctWindowPosition(Stage stage) {
        Rectangle2D visualBounds = Screen.getPrimary().getVisualBounds();

        // Устанавливаем окно в видимую область с отступами
        double width = visualBounds.getWidth() * 0.8;
        double height = visualBounds.getHeight() * 0.8;

        stage.setWidth(width);
        stage.setHeight(height);
        stage.setX(visualBounds.getMinX() + (visualBounds.getWidth() - width) / 2);
        stage.setY(visualBounds.getMinY() + (visualBounds.getHeight() - height) / 2);

        // Дополнительная проверка через короткое время
        Platform.runLater(() -> {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {}

            Platform.runLater(() -> {
                double currentY = stage.getY();
                double currentHeight = stage.getHeight();
                double screenBottom = visualBounds.getMaxY();

                // Если окно выходит за нижнюю границу - поднимаем его
                if (currentY + currentHeight > screenBottom) {
                    double overflow = (currentY + currentHeight) - screenBottom;
                    stage.setY(currentY - overflow - 10);
                }
            });
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}