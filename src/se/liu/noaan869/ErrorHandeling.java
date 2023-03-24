package se.liu.noaan869;

import java.io.IOException;

public class ErrorHandeling {
    private HighscoreList hslist;

    public ErrorHandeling(HighscoreList hslist) {
        this.hslist = hslist;
    }

    public void ErrorCreateFile(boolean newFile) {
        if(!newFile) {
            if(TetrisViewer.showErrorMessage("failed to create new file!")) {
                this.hslist.readDataFromHScoreFile();
            }else {
                System.exit(1);
            }
        }
    }

    public void ErrorIOException(IOException ee) {
        if(TetrisViewer.showErrorMessage(ee.getMessage())) {
            this.hslist.readDataFromHScoreFile();
        }else {
            System.exit(1);
        }
        ee.printStackTrace();
    }

    public void ErrorDeleteFile(boolean deleted) {
        if (!deleted) {
            TetrisViewer.showErrorMessage("file could not be deleted!");
        }

    }

    public void ErrorRenameFile(boolean flag) {
        if (!flag) {
            TetrisViewer.showErrorMessage("file could not be renamed/moved!");
        }
    }

}
