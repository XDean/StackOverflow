package xdean.stackoverflow;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.base.Charsets;
import com.google.common.io.LineProcessor;
import com.google.common.io.Resources;

import io.reactivex.Flowable;
import io.reactivex.Scheduler;
import xdean.jex.extra.rx2.RxSchedulers;
import xdean.jex.util.file.FileUtil;

public class ReadMeGenerator {

  public static void main(String[] args) {
    Scheduler scheduler = RxSchedulers.fixedSize(10);
    FileUtil.deepTraversal(Paths.get("src", "main", "java"))
        .map(p -> p.getFileName().toString())
        .map(p -> p.endsWith(".java") ? p.substring(0, p.length() - 5) : p)
        .filter(p -> p.matches("Q[0-9]*(.java)?"))
        .map(p -> Integer.parseInt(p.substring(1)))
        .flatMap(i -> Flowable.just(i).map(ReadMeGenerator::getName).subscribeOn(scheduler))
        .filter(Optional::isPresent)
        .map(Optional::get)
        .map(s -> resolve(s))
        .blockingSubscribe(name -> {
          System.out.println(name);
        });
    ;
  }

  public static final Pattern ASCII_PATTERN = Pattern.compile("&#([0-9]{1,3});");

  private static String resolve(String s) {
    while (true) {
      Matcher matcher = ASCII_PATTERN.matcher(s);
      if (matcher.find() == false) {
        break;
      }
      int start = matcher.start();
      int end = matcher.end();
      char c = (char) Integer.parseInt(matcher.group(1));
      s = s.substring(0, start) + c + s.substring(end);
    }
    return s;
  }

  public static final String QUESTION_URL = "https://stackoverflow.com/questions/";
  public static final Pattern TITLE_PATTERN = Pattern.compile("<title>(.*)</title>");

  private static Optional<String> getName(int id) {
    try {
      return Resources.readLines(new URL(QUESTION_URL + id), Charsets.UTF_8,
          new LineProcessor<Optional<String>>() {
            String title;

            @Override
            public boolean processLine(String line) throws IOException {
              Matcher matcher = TITLE_PATTERN.matcher(line);
              if (matcher.find()) {
                title = matcher.group(1);
                return false;
              } else {
                return true;
              }
            }

            @Override
            public Optional<String> getResult() {
              return Optional.ofNullable(title);
            }
          });
    } catch (Exception e) {
      e.printStackTrace();
      return Optional.empty();
    }
  }
}
