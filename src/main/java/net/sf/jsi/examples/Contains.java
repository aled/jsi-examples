package net.sf.jsi.examples;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gnu.trove.procedure.TIntProcedure;

import net.sf.jsi.SpatialIndex;
import net.sf.jsi.rtree.RTree;
import net.sf.jsi.Rectangle;

public class Contains {
  private static final Logger log = LoggerFactory.getLogger(Contains.class);
  
  public static void main(String[] args) {
    new Contains().run();
  }
 
  private void run() {
    // Create and initialize an rtree
    SpatialIndex si = new RTree();
    si.init(null);
    
    // We have some points or rectangles in some other data structure.
    // The rtree can handle millions of these.
    Rectangle[] rects = new Rectangle[] {
        new Rectangle(0, 0, 0, 0),
        new Rectangle(0, 1, 0, 1),
        new Rectangle(1, 0, 1, 0),
        new Rectangle(1, 1, 1, 1),
    };
    
    // Add our data to the rtree. Every time we add a rectangle we give it
    // an ID. This ID is what is returned by querying the rtree. In this 
    // example we use the array index as the ID.
    for (int i = 0; i < rects.length; i++) {
      si.add(rects[i], i);
    }
 
    // Now see which of these points is contained by some
    // other rectangle. The rtree returns the results of a query
    // by calling the execute() method on a TIntProcedure.
    // In this example we want to save the results of the query 
    // into a list, so that's what the execute() method does.
    class SaveToListProcedure implements TIntProcedure {
      private List<Integer> ids = new ArrayList<Integer>();

      public boolean execute(int id) {
        ids.add(id);
        return true;
      }; 
      
      private List<Integer> getIds() {
        return ids;
      }
    };
    
    SaveToListProcedure myProc = new SaveToListProcedure();
    si.contains(new Rectangle(-0.5f, -0.5f, 1.5f, 0.5f), myProc);
    
    List<Integer> ids = myProc.getIds();
    
    for (Integer id : ids) {
      log.info(rects[id].toString() + " was contained");
    }
    
    // Actually that was a really long winded (and inefficient) way of 
    // printing out the rectangles. Would be better to use an anonymous
    // class to just do the printing inside the execute method. That is
    // what the NearestN class does.
  }
}

