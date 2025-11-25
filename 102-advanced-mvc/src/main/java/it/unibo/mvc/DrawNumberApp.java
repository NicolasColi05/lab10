package it.unibo.mvc;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

/**
 */
public final class DrawNumberApp implements DrawNumberViewObserver {

    private final DrawNumber model;
    private final List<DrawNumberView> views;

    /**
     * @param views the views to attach
     * 
     * @throws IOException .
     */
    public DrawNumberApp(final DrawNumberView... views) throws IOException {
        int count = 0;
        int min = 0;
        int max = 100;
        int attempt = 10;
        /*
         * Side-effect proof
         */
        this.views = Arrays.asList(Arrays.copyOf(views, views.length));
        for (final DrawNumberView view: views) {
            view.setObserver(this);
            view.start();
        }
        try (BufferedReader br = new BufferedReader(new InputStreamReader(//NOPMD just for exercise
            new FileInputStream("src/main/resources/config.yml")))) { 
                final List<String> list = new ArrayList<>();
                list.add(br.readLine());
                list.add(br.readLine());
                list.add(br.readLine());
                for (final String string : list) {
                    final StringTokenizer st = new StringTokenizer(string, ": ");
                    while (st.hasMoreTokens()) {
                        st.nextToken();
                        final String number = st.nextToken();
                        switch (count) {
                            case 0: min = Integer.parseInt(number); 
                            break;
                            case 1: max = Integer.parseInt(number);
                            break;
                            case 2: attempt = Integer.parseInt(number); 
                            break;
                            default: break;
                        }
                        count++;
                    }
                }
        } catch (final FileNotFoundException e) {
            e.printStackTrace(); //NOPMD
            for (final DrawNumberView drawNumberView : views) {
                drawNumberView.displayError("file not found");
            }
        }
        this.model = new DrawNumberImpl(min, max, attempt);
    }

    @Override
    public void newAttempt(final int n) {
        try {
            final DrawResult result = model.attempt(n);
            for (final DrawNumberView view: views) {
                view.result(result);
            }
        } catch (final IllegalArgumentException e) {
            for (final DrawNumberView view: views) {
                view.numberIncorrect();
            }
        }
    }

    @Override
    public void resetGame() {
        this.model.reset();
    }

    @Override
    public void quit() {
        /*
         * A bit harsh. A good application should configure the graphics to exit by
         * natural termination when closing is hit. To do things more cleanly, attention
         * should be paid to alive threads, as the application would continue to persist
         * until the last thread terminates.
         */
        System.exit(0);
    }

    /**
     * @param args
     *            ignored
     * @throws IOException .
     */
    public static void main(final String... args) throws IOException {
        new DrawNumberApp(new DrawNumberViewImpl(), new DrawNumberViewImpl(), new PrintStreamView("src/output.txt"),
        new PrintStreamView("/dev/stdout"));
    }

}
