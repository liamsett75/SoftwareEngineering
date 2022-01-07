package FileReaders;

//This interface makes sure that all file readers must implement the read file function.
public interface FileReader {
    String splitBy = ","; //used to split each line in every csv file

    public void readFile(String fileName); //this function needs to be implemented in each class that extends FileReader
}
