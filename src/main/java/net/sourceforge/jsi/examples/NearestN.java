package net.sourceforge.jsi.examples;

import org.slf4j.*;
import com.infomatiq.jsi.*;
import gnu.trove.*;

import com.infomatiq.jsi.Rectangle;
import com.infomatiq.jsi.rtree.RTree;

public class NearestN {
  private static final Logger log = LoggerFactory.getLogger(NearestN.class);
 
  public static void main(String[] args) {
    new NearestN().run();
  }
 
  private class NullProc implements TIntProcedure {
    public boolean execute(int i) {
      return true;
    }
  }
    
  private void run() {
    int rowCount = 1000;
    int columnCount = 1000;
    int count = rowCount * columnCount;
    long start, end;
    
    log.info("Creating " + count + " rectangles");
    final Rectangle[] rects = new Rectangle[count];
    int id = 0;
    for (int row = 0; row < rowCount; row++)
      for (int column = 0; column < rowCount; column++) {
        rects[id++] = new Rectangle(row, column, row+0.5f, column+0.5f); // 
    }
    
    log.info("Indexing " + count + " rectangles");
    start = System.currentTimeMillis();
    SpatialIndex si = new RTree();
    si.init(null);
    for (id=0; id < count; id++) {
      si.add(rects[id], id);
    }
    end = System.currentTimeMillis();
    log.info("Average time to index rectangle = " + ((end - start) / (count / 1000.0)) + " us");
    
    final Point p = new Point(36.3f, 84.3f);
    log.info("Querying for the nearest 3 rectangles to " + p);
    si.nearestN(p, new TIntProcedure() {
      public boolean execute(int i) {
        log.info("Rectangle " + i + " " + rects[i] + ", distance=" + rects[i].distance(p));
        return true;
      }
    }, 3, Float.MAX_VALUE);

    // Run a performance test, find the 3 nearest rectangles
    final int[] ret = new int[1];
    log.info("Running 10000 queries for the nearest 3 rectangles");
    start = System.currentTimeMillis();
    for (int row = 0; row < 100; row++) {
      for (int column = 0; column < 100; column++) {
        p.x = row + 0.6f;
        p.y = column + 0.7f;
        si.nearestN(p, new TIntProcedure() {
          public boolean execute(int i) {
            ret[0]++;
            return true; // don't do anything with the results, for a performance test.
          }
        }, 3, Float.MAX_VALUE);
      }
    }
    end = System.currentTimeMillis();
    log.info("Average time to find nearest 3 rectangles = " + ((end - start) / (10000 / 1000.0)) + " us");
    log.info("total time = " + (end - start) + "ms");
    log.info("total returned = " + ret[0]);
    
    // Run a performance test, find the 3 nearest rectangles
    log.info("Running 30000 queries for the nearest 3 rectangles");
    
    TIntProcedure proc = new NullProc();
    start = System.currentTimeMillis();
    for (int row = 0; row < 300; row++) {
      for (int column = 0; column < 100; column++) {
        p.x = row + 0.6f;
        p.y = column + 0.7f;
        si.nearestN(p, proc, 3, Float.MAX_VALUE);
      }
    }
    end = System.currentTimeMillis();
    log.info("Average time to find nearest 3 rectangles = " + ((end - start) / (30000 / 1000.0)) + " us");
    log.info("total time = " + (end - start) + "ms");
    log.info("total returned = " + ret[0]);
  }
}
