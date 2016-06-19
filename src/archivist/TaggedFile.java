/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package archivist;

import java.nio.file.Path;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author marouano
 */
public class TaggedFile {
    public Path filePath;
    ObservableList<String> Tags ;
    public String Note;
    
    public TaggedFile(Path filePath) {
        this.filePath = filePath;
        Tags = FXCollections.observableArrayList ();
    }
    
    public void addTag(String str){
        if (!str.trim().isEmpty())
        {
            Tags.add(str);
        }
    }
    public void addNote(String str){
        if (!str.trim().isEmpty())
        {
            Note = str;
        }
    }
}
