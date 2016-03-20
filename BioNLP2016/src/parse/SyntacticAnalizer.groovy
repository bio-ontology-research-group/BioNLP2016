package parse

import edu.stanford.nlp.ling.CoreAnnotations
import edu.stanford.nlp.ling.CoreLabel
import edu.stanford.nlp.ling.Label
import edu.stanford.nlp.pipeline.Annotation
import edu.stanford.nlp.pipeline.StanfordCoreNLP
import edu.stanford.nlp.semgraph.SemanticGraph
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations
import edu.stanford.nlp.trees.GrammaticalRelation
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations
import edu.stanford.nlp.trees.TypedDependency;
import edu.stanford.nlp.util.CoreMap
import org.apache.commons.io.IOUtils

def printTree
printTree={ Tree node ->
    if((node==null)||(node.isLeaf())){
        return;
    }
    System.out.println(node.value())
    if(node.value().equals("NP") ) {
        List<Tree> leaves = node.getLeaves();
        for(Tree leaf : leaves) {
            System.out.print(leaf.toString()+" ");

        }
    }
    for(Tree child : node.children()) {
        printTree(child);
    }
}

String trainingPath="../../BioNLP_Resources/BioNLP-ST-2016_BB-cat_train/"

ArrayList<File> fileList= new ArrayList<File>()

new File(trainingPath).eachFileMatch(~/.*.txt/){file ->
    fileList.add(file)
}

String content = IOUtils.toString(new FileReader(fileList.get(0)));

// creates a StanfordCoreNLP object, with POS tagging, lemmatization, NER, parsing, and coreference resolution
Properties props = new Properties();
props.setProperty("ner.useSUTime", "false")
props.put("annotators", "tokenize, ssplit, pos, lemma, ner, parse, dcoref");
StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

Annotation document = new Annotation(content);

pipeline.annotate(document);

List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);

for(CoreMap sentence: sentences) {
    for (CoreLabel token: sentence.get(CoreAnnotations.TokensAnnotation.class)) {
        // this is the text of the token
        String word = token.get(CoreAnnotations.TextAnnotation.class);
        // this is the POS tag of the token
        String pos = token.get(CoreAnnotations.PartOfSpeechAnnotation.class);
        // this is the NER label of the token
        String ne = token.get(CoreAnnotations.NamedEntityTagAnnotation.class);

        System.out.println(word+"-->"+pos+"-->"+ne);
    }

    // this is the parse tree of the current sentence
    Tree tree = sentence.get(TreeCoreAnnotations.TreeAnnotation.class);

    printTree(tree);

    // this is the Stanford dependency graph of the current sentence
    SemanticGraph dependencies = sentence.get(SemanticGraphCoreAnnotations.CollapsedCCProcessedDependenciesAnnotation.class);
    Collection<TypedDependency> deps = dependencies.typedDependencies();
    for (TypedDependency typedDep : deps) {
        GrammaticalRelation reln = typedDep.reln();
        String type = reln.toString();
        System.out.println(type);
    }
}

