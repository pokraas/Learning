package Week1;

public class Outcast {
	private WordNet wordnet;
	public Outcast(WordNet wordnet) {
		this.wordnet=wordnet;
	}
	
	public String outcast(String[] nouns) {
		if (nouns==null) throw new IllegalArgumentException();
		int maxDist=0;
		String outcast=null;
		for (String nounA : nouns) {
			if (nounA==null) throw new IllegalArgumentException();
			int dist=0;
			for (String nounB : nouns) {
				dist+=wordnet.distance(nounA, nounB);
				//System.out.println(nounA+" - "+dist+" - "+nounB);
			}
			if (dist>maxDist) {
				maxDist=dist;
				outcast=nounA;
			}
		}
		return outcast;
	}

	public static void main(String[] args) {
		Outcast o = new Outcast(new WordNet("Week1/synsets.txt", "Week1/hypernyms.txt"));
		System.out.println(o.outcast(new String[] {
			"horse","cat","tiger","table","horse"
		}));
	}

}
