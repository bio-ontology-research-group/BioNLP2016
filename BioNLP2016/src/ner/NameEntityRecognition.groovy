package ner

import martin.common.ArgParser
import martin.common.Loggers
import uk.ac.man.documentparser.input.TextFile
import uk.ac.man.entitytagger.EntityTagger
import uk.ac.man.entitytagger.matching.MatchOperations
import uk.ac.man.entitytagger.matching.Matcher

import java.util.logging.Logger

def executeSR4GN(String outpath,String setupFile,File file){
    //Example perl SR4GN.pl -s setup.txt -i input -o output
    // Setup: User can choose to use full capability of SR4GN ("setup.txt") or just the species recognition functionality ("setup_SR.txt"). The default is "setup_SR.txt".
	// Input: User can provide the input data folder route ("input" or "C:\input").
	// Input: User can provide the output data folder route ("output" or "C/Documents/Projects/Miguel/BioNLP2016/BioNLP2016/libs/sr4gn/SR4GN_unix/"
    String sentence ="perl "+sr2gnPath+"SR4GN.pl -s "+sr2gnPath+setupFile+" -i "+file.getAbsolutePath()+" -o "+outpath;
    Process process= sentence.execute();
    System.out.println(process.text)
}

def executeLinnaeusNER={String outpath,String pmcConfFile,String nerConfFile,int numThreads,File[] documentsDirectory->
    //Example java -Xmx4G -jar linnaeus-2.0.jar --properties pmc/properties.conf species/properties.conf --out mentions.tsv --report 100 --threads
    String[] arrayArgs = new String[9];
    arrayArgs[0]="--properties"
    arrayArgs[1]=pmcConfFile
    arrayArgs[2]=nerConfFile
    arrayArgs[3]="--out"  // All tags will be saved to the specified file, in a tab-separated format giving species ID(s), document ID, start and end coordinate and the matched term.
    arrayArgs[4]=outpath
    arrayArgs[5]="--report" //Cause the software to show progress after each <interval> document
    arrayArgs[6]="-1"
    arrayArgs[7]="--threads" //Specify the number of threads the application should use
    arrayArgs[8]=numThreads

    ArgParser ap = new ArgParser(arrayArgs);
    Logger logger = Loggers.getDefaultLogger(ap);
    int report = ap.getInt("report");
    File outPath = new File(ap.get("out"))
    //load the matcher
    Matcher matcher = EntityTagger.getMatcher(ap,logger);

    //create an iterator for the document set

    TextFile textFile = new TextFile(documentsDirectory);
    MatchOperations.runToFile(matcher,textFile, numThreads, report, outPath, logger);
}


String trainingPath="../../BioNLP_Resources/BioNLP-ST-2016_BB-cat_train/"

ArrayList<File> fileList= new ArrayList<File>()

new File(trainingPath).eachFileMatch(~/.*.txt/){file ->
    fileList.add(file)
}
File[] trainingFiles= fileList.toArray(new File[fileList.size()]);

//executeLinnaeusNER("../../outputs/mentions_species.tsv","../../libs/linnaeus/pmc/properties.conf","../../libs/linnaeus/species/properties.conf",4,trainingFiles)
//executeLinnaeusNER("../../outputs/mentions_species-proxy.tsv","../../libs/linnaeus/pmc/properties.conf","../../libs/linnaeus/species-proxy/properties.conf",4,trainingFiles)
executeSR4GN("../../outputs/mentions_species_sr4gn.tsv","setup.txt",trainingFiles[0]);
System.out.println("finish");