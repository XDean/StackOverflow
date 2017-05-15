package xdean.stackoverflow.rx;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import lombok.SneakyThrows;
import rx.Observable;
import rx.observables.JavaFxObservable;
import rx.schedulers.Schedulers;

// abandon
public class Q43794205 extends Application {
  public static void main(String[] args) {
    launch(args);
  }

  @SneakyThrows
  private boolean request(String taskName) {
    System.out.printf("Task %s start in %s.\n", taskName, Thread.currentThread());
    Thread.sleep(3000);
    System.out.printf("Task %s end in %s.\n", taskName, Thread.currentThread());
    return Math.random() > 0.5;
  }

  private Button buttonA, buttonB;

  @Override
  public void start(Stage stage) throws Exception {
    stage.setScene(new Scene(new HBox(
        buttonA = new Button("A"),
        buttonB = new Button("B"))
        ));
    stage.show();

    Observable<String> events = Observable.merge(
        JavaFxObservable.fromActionEvents(buttonA).map(e -> "A"),
        JavaFxObservable.fromActionEvents(buttonB).map(e -> "B"));
    events.observeOn(Schedulers.io())
    
        .concatMap(e -> events.startWith(e).map(this::request))
        .subscribe();
  }
}
