package core.utils;

import javafx.animation.*;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.CacheHint;
import javafx.scene.Node;
import javafx.util.Duration;

public class ShakeWindow extends Transition {
    private final Timeline timeline;
    private final Node node;
    private final boolean useCache = true;
    private final double xIni;

    private boolean oldCache = false;
    private CacheHint oldCacheHint = CacheHint.DEFAULT;

    public ShakeWindow(final Node node, EventHandler<ActionEvent> event) {
        this.node = node;

        statusProperty().addListener((ov, t, newStatus) -> {

            if (newStatus == Status.RUNNING) this.starting();
            else this. stopping();

        });

        Interpolator WEB_EASE = Interpolator.SPLINE(0.25, 0.3, 0.45, 1);
        DoubleProperty x = new SimpleDoubleProperty();
        this.timeline = new Timeline(
                new KeyFrame(Duration.millis(0), new KeyValue(x, 0, WEB_EASE)),
                new KeyFrame(Duration.millis(100), new KeyValue(x, -10, WEB_EASE)),
                new KeyFrame(Duration.millis(200), new KeyValue(x, 10, WEB_EASE)),
                new KeyFrame(Duration.millis(300), new KeyValue(x, -10, WEB_EASE)),
                new KeyFrame(Duration.millis(400), new KeyValue(x, 10, WEB_EASE)),
                new KeyFrame(Duration.millis(500), new KeyValue(x, -10, WEB_EASE)),
                new KeyFrame(Duration.millis(600), new KeyValue(x, 10, WEB_EASE)),
                new KeyFrame(Duration.millis(700), new KeyValue(x, -10, WEB_EASE)),
                new KeyFrame(Duration.millis(800), new KeyValue(x, 10, WEB_EASE)),
                new KeyFrame(Duration.millis(900), new KeyValue(x, -10, WEB_EASE)),
                new KeyFrame(Duration.millis(1000), new KeyValue(x, 0, WEB_EASE))
        );

        xIni = node.getScene().getWindow().getX();
        x.addListener((ob, n, n1) -> {

            (node.getScene().getWindow()).setX(xIni+n1.doubleValue());
        });

        setCycleDuration(Duration.seconds(1));
        setDelay(Duration.seconds(0.2));
        setOnFinished(event);
    }


    private void starting() {
        if (useCache) {
            oldCache = node.isCache();
            oldCacheHint = node.getCacheHint();
            node.setCache(true);
            node.setCacheHint(CacheHint.SPEED);
        }
    }

    private void stopping() {
        if (useCache) {
            node.setCache(oldCache);
            node.setCacheHint(oldCacheHint);
        }
    }

    @Override
    protected void interpolate(double d) {
        timeline.playFrom(Duration.seconds(d));
        timeline.stop();
    }
}
