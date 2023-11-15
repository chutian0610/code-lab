package info.victorchu.demos.jol.quickstart;

import org.openjdk.jol.datamodel.DataModel;
import org.openjdk.jol.datamodel.Model32;
import org.openjdk.jol.datamodel.Model64;
import org.openjdk.jol.info.ClassLayout;
import org.openjdk.jol.layouters.CurrentLayouter;
import org.openjdk.jol.layouters.HotSpotLayouter;
import org.openjdk.jol.layouters.Layouter;

public class JolCase08 {
     private static final DataModel[] MODELS_JDK8 = new DataModel[]{
            new Model32(),
            new Model64(false, false),
            new Model64(true, true),
            new Model64(true, true, 16),
    };

    private static final DataModel[] MODELS_JDK15 = new DataModel[]{
            new Model64(false, true),
            new Model64(false, true, 16),
    };

    public static void main(String[] args) {
        {
            Layouter l = new CurrentLayouter();
            System.out.println("***** " + l);
            System.out.println(ClassLayout.parseClass(A.class, l).toPrintable());
        }

        for (DataModel model : MODELS_JDK8) {
            Layouter l = new HotSpotLayouter(model, 8);
            System.out.println("***** " + l);
            System.out.println(ClassLayout.parseClass(A.class, l).toPrintable());
        }

        for (DataModel model : MODELS_JDK8) {
            Layouter l = new HotSpotLayouter(model, 15);
            System.out.println("***** " + l);
            System.out.println(ClassLayout.parseClass(A.class, l).toPrintable());
        }

        for (DataModel model : MODELS_JDK15) {
            Layouter l = new HotSpotLayouter(model, 15);
            System.out.println("***** " + l);
            System.out.println(ClassLayout.parseClass(A.class, l).toPrintable());
        }
    }

    public static class A {
        Object a;
        int b;
    }
}
