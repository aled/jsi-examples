package net.sourceforge.jsi.examples;

import org.slf4j.*;
import com.infomatiq.jsi.*;
import gnu.trove.*;

import com.infomatiq.jsi.Rectangle;
import com.infomatiq.jsi.rtree.RTree;

public class NearestN {
  private static final Logger log = LoggerFactory.getLogger(NearestN.class);
  
  public static void main(String[] args) {
    int count = 1000000;
    log.info("Creating " + count + " rectangles");
    
    final Rectangle[] rects = new Rectangle[count];
    for (int id=0; id < count; id++) {
      rects[id] = new Rectangle(id, id, id+0.5f, id+0.5f); // 
    }
    
    log.info("Indexing " + count + " rectangles");
    SpatialIndex si = new RTree();
    si.init(null);
    for (int id=0; id < count; id++) {
      si.add(rects[id], id);
    }
    
    final Point p = new Point(36.3f, 84.3f);
    log.info("Querying for the nearest 3 rectangles to " + p);
    si.nearestN(p, new TIntProcedure() {
      public boolean execute(int i) {
        log.info("Rectangle " + i + " " + rects[i] + ", distance=" + rects[i].distance(p));
        return true;
      }
    }, 10, Float.MAX_VALUE);
  }
}
