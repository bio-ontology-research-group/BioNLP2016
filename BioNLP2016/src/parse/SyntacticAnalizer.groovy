package parse

import edu.stanford.nlp.semgraph.SemanticGraph
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations
import edu.stanford.nlp.trees.*

GrammaticalStructure.readCoNLLXGrammaticalStructureCollection()
GrammaticalStructure gs = gsf.newGrammaticalStructure(parse);
Collection tdl = gs.typedDependenciesCollapsed();


Tree tree = sentenceAnnotation.get(TreeCoreAnnotations.TreeAnnotation.class);
// print the tree if needed
SemanticGraph basic = sentenceAnnotation.get(SemanticGraphCoreAnnotations.BasicDependenciesAnnotation.class);
Collection<TypedDependency> deps = basic.typedDependencies();
for (TypedDependency typedDep : deps) {
    GrammaticalRelation reln = typedDep.reln();
    String type = reln.toString();
}

SemanticGraph colapsed = sentenceAnnotation
        .get(SemanticGraphCoreAnnotations.CollapsedDependenciesAnnotation.class);
deps = colapsed.typedDependencies();
for (TypedDependency typedDep : deps) {
    GrammaticalRelation reln = typedDep.reln();
    String type = reln.toString();
}

SemanticGraph ccProcessed = sentenceAnnotation
        .get(SemanticGraphCoreAnnotations.CollapsedCCProcessedDependenciesAnnotation.class);
deps = ccProcessed.typedDependencies();
for (TypedDependency typedDep : deps) {
    GrammaticalRelation reln = typedDep.reln();
    String type = reln.toString();
}