import java.util.ArrayList;
import java.util.List;

import de.bioutils.fasta.FASTAElement;
import de.bioutils.fasta.NewFASTAFile;
import de.bioutils.fasta.NewFASTAFileImpl;


public class FASTAFileBuilder {
	
	private final List<FASTAElement> elements = new ArrayList<FASTAElement>();

	public void addElement(FASTAElement fastaElement) {
		elements.add(fastaElement);
	}

	public NewFASTAFile build() {
		return new NewFASTAFileImpl(elements);
	}

}
