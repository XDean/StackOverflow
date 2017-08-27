package xdean.stackoverflow.rx;

import static xdean.jex.util.lang.ExceptionUtil.uncheck;
import io.reactivex.rxjavafx.schedulers.JavaFxScheduler;
import io.reactivex.subjects.PublishSubject;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class Q45897796 extends Application {
  public static void main(String[] args) {
    launch(args);
  }

  private Group root;
  private List<Rectangle> rectangles = new LinkedList<>();

  @Override
  public void start(Stage primaryStage) throws InterruptedException {
    root = new Group();
    Scene scene = new Scene(root, 500, 500, Color.WHITE);
    primaryStage.show();
    primaryStage.setScene(scene);

    Sorter sorter = new Sorter();
    sorter.getSubject()
        .doOnNext(a -> Thread.sleep(1000))
        .observeOn(JavaFxScheduler.platform())
        .subscribe(this::drawRectangles);
    new Thread(() -> uncheck(() -> sorter.bubbleSort(100))).start();
  }

  private void drawRectangles(List<Integer> integers) {
    root.getChildren().clear();
    rectangles.clear();
    for (int i = 0; i < integers.size(); i++) {
      Rectangle rectangle = new Rectangle(i * 20, 500 - integers.get(i), 10, integers.get(i));
      rectangle.setFill(Color.BLACK);
      rectangles.add(rectangle);
    }
    root.getChildren().addAll(rectangles);
  }

  static class Sorter {
    private PublishSubject<List<Integer>> subject = PublishSubject.create();

    public void bubbleSort(int delay) throws InterruptedException {
      List<Integer> ints = randomIntegers(0, 300, 20);
      for (int i = 0; i < ints.size(); i++) {
        for (int j = 0; j < ints.size() - 1; j++) {
          if (ints.get(j) > ints.get(j + 1)) {
            Collections.swap(ints, j, j + 1);
            subject.onNext(ints);
            System.out.println("sort");
            Thread.sleep(delay);
          }
        }
      }
    }

    private List<Integer> randomIntegers(int origin, int bound, int limit) {
      return new Random()
          .ints(origin, bound)
          .limit(limit)
          .boxed()
          .collect(Collectors.toList());
    }

    public PublishSubject<List<Integer>> getSubject() {
      return subject;
    }
  }
}
