/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package archivist;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javax.activation.MimetypesFileTypeMap;

/**
 *
 * @author marouano
 */
public class FXMLDocumentController implements Initializable {
    File selectedFile;
    boolean selectedFileIsTagged = false;
    ArrayList<TaggedFile> taggedFiles;
    @FXML
    TreeView theFileTree;
    @FXML
    ImageView imgView;
    @FXML
    Button addTagButton;
    @FXML
    TextField tagField;
    @FXML
    ListView tagList;
    @FXML
    TextArea NoteTB;
    @FXML
    Button NoteBTN;
    @FXML
    TextArea FileInfoTA;
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        taggedFiles = new ArrayList<>();
        TreeItem<File> root = createNode(new File(System.getProperty("user.home")));
        theFileTree.setRoot(root);
        // Eventhandler for changing the selection
        theFileTree.getSelectionModel().selectedItemProperty().addListener( new ChangeListener() {

            @Override
            public void changed(ObservableValue observable, Object oldValue,
                Object newValue) {

                    TreeItem<File> selectedItem = (TreeItem<File>) newValue;
                    selectedFile = selectedItem.getValue();
                    selectedFileIsTagged = false;
                    System.out.println("selectedFile = " + selectedFile.getPath());
                    tagList.setItems(null);
                    NoteTB.setText(null);
                    FileInfoTA.setText(
                        "Name: " + selectedFile.getName() + "\n" +
                        "Absolute path: " + selectedFile.getAbsolutePath() + "\n" +
                        "Size: " + selectedFile.length() + "\n" + 
                        "Last modified: " + new Date(selectedFile.lastModified())
                    );
                    
                    // Check if selectedFile is an image and if it is display the image in the imageview
                    String mimetype= new MimetypesFileTypeMap().getContentType(selectedFile);
                    String type = mimetype.split("/")[0];
                    if (type.equals("image"))
                    {
                        System.out.println("This is a image and this is it's URL(?): " + selectedFile.toURI().toString());
                        Image img = new Image(selectedFile.toURI().toString());
                        imgView.setImage(img);
                        
                    }
                    else 
                    {
                        System.out.println("Not an IMage");
                        imgView.setImage(null);
                    }
                    
                    // Check if the selected file has tags
                    for (TaggedFile f : taggedFiles)
                    {
                        if (f.filePath == selectedFile.toPath())
                        {
                            selectedFileIsTagged = true;
                            tagList.setItems(f.Tags);
                            NoteTB.setText(f.Note);
                        }
                    }
            }
        });
        
        // Listen for TextField text changes
        tagField.textProperty().addListener(new ChangeListener<String>() {
        @Override
        public void changed(ObservableValue<? extends String> observable,
            String oldValue, String newValue) {
                if (newValue.trim().isEmpty())
                {
                    addTagButton.setDisable(true);
                }
                else 
                {
                    addTagButton.setDisable(false);
                }
            }
        });
    }    
    

    // Populate file tree with file's
    private TreeItem<File> createNode(final File f) {
        return new TreeItem<File>(f) {
            private boolean isLeaf;
            private boolean isFirstTimeChildren = true;
            private boolean isFirstTimeLeaf = true;

            @Override
            public ObservableList<TreeItem<File>> getChildren() {
                if (isFirstTimeChildren) {
                    isFirstTimeChildren = false;
                    super.getChildren().setAll(buildChildren(this));
                }
                return super.getChildren();
            }

            @Override
            public boolean isLeaf() {
                if (isFirstTimeLeaf) {
                    isFirstTimeLeaf = false;
                    File f = (File) getValue();
                    isLeaf = f.isFile();
                }
                return isLeaf;
            }

            private ObservableList<TreeItem<File>> buildChildren(TreeItem<File> TreeItem) {
                File f = TreeItem.getValue();
                if (f == null) {
                    return FXCollections.emptyObservableList();
                }
                if (f.isFile()) {
                    return FXCollections.emptyObservableList();
                }
                File[] files = f.listFiles();
                if (files != null) {
                    ObservableList<TreeItem<File>> children = FXCollections.observableArrayList();
                    for (File childFile : files) {
                        children.add(createNode(childFile));
                    }
                    return children;
                }
                return FXCollections.emptyObservableList();
            }
        };
    }
    
    @FXML
    private void addTagBtnPress(ActionEvent event)
    {
        if (!(tagField.getText().trim().isEmpty()))
        {
            if (!selectedFileIsTagged)
            {
                TaggedFile f = new TaggedFile(selectedFile.toPath());
                f.addTag(tagField.getText().trim());
                taggedFiles.add(f);
                tagList.setItems(f.Tags);
                System.out.println(tagField.getText().trim());
                tagField.clear();
            }
            else
            {
                for (TaggedFile tf : taggedFiles)
                {
                    tf.addTag(tagField.getText().trim());
                    tagField.clear();
                }
            }

        }
        else
        {
            
            System.out.println("tagField is empty");
        }   
    }
    
    @FXML
    private void saveNoteBtnPress(ActionEvent event)
    {
        if (!(NoteTB.getText().trim().isEmpty()))
        {
            if (!selectedFileIsTagged)
            {
                TaggedFile f = new TaggedFile(selectedFile.toPath());
                f.addNote(NoteTB.getText().trim());
                taggedFiles.add(f);
                NoteTB.setText(f.Note);
            }
            else
            {
                for (TaggedFile tf : taggedFiles)
                {
                    tf.addTag(tagField.getText().trim());
                    tagField.clear();
                }
            }
        }
        else
        {
            System.out.println("tagField is empty");
        }   
    }
}
