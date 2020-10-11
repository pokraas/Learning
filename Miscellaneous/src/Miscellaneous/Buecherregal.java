package Miscellaneous;

class Buch{
	private String titel, author;
	Buch(String t, String a){
		titel = t;
		author = a;
	}
	public String titelAusgeben() {
		return titel;
	}
	public String authorAusgeben() {
		return author;
	}
}

class Buecherregal { 
    private Buch[] regal = new Buch[10]; 
     
    // Fuegen das neue Attribut hier ein 
    int anzahlBuecher = 0; 
 
    // Fuegen Sie die neuen Methoden hier ein 
    int kapazitaet(){ 
        return regal.length; 
    } 
    int anzahlBuecher(){ 
         return anzahlBuecher; 
    } 
     
     /*
    public void buchEinstellen(Buch b){ 
        int i=0; 
            if(anzahlBuecher == regal.length){ 
                Buch[] regaltemp = new Buch[regal.length * 2]; 
                regaltemp[i*2] = regal[i]; 
            } 
            if(i < regal.length){ 
                regal[i] = b;       
                i++; 
                anzahlBuecher++; 
            } 
    } 
    */
    
    public void buchEinstellen(Buch b) {
    	if (anzahlBuecher == regal.length) {
    		//Doubling the size of regal
    		Buch[] regaltemp = new Buch[regal.length * 2];
    		//Taking all the books from regal into regaltemp
    		for (int i=0; i<regal.length; i++) regaltemp[i] = regal[i];
    		//Reassigning regal
    		regal = regaltemp;
    	}
    	//Putting the Buch b onto the next free index
    	int nextFreeIndex = 0;
    	for (int i=0; i<regal.length; i++) {
			if (regal[i]==null) {
				nextFreeIndex = i;
				break;
			}
    	}
    	regal[nextFreeIndex] = b;
    	anzahlBuecher++;
    	
    }
     
     
    public Buch buchMitTitelFinden(String titel){        
        for(int i = 0; i < anzahlBuecher; i++){ 
            if(regal[i] != null){ 
                if (regal[i].titelAusgeben().equals(titel)){    
                    return regal[i]; 
                } 
            } 
        } 
        return null; 
    } 
     
 
    //Hier lauft was falsch 
    public void buchMitTitelEntfernen(String titel){ 
    	System.out.println(titel+" will be deleted...");
        for(int i = 0; i < anzahlBuecher; i++){ 
            if(regal[i] != null){ 
                if(regal[i].titelAusgeben().equals(titel)){ 
                	System.out.println("Found the Buch to delete");
                    while(i<anzahlBuecher){ 
                        regal[i] = regal[i+1]; 
                        i++; 
                    } 
                    System.out.println("Decreasing anzahlBuecher");
                    anzahlBuecher--; 
                }
            } 
        } 
    } 
     
 
 
    public static void main(String[] args) { 
        Buecherregal br = new Buecherregal(); 
        Buch b1 = new Buch("Ein Freund fuer Nanoka", "Saro Tekkotsu"); 
        Buch b2 = new Buch("2 Freunde fuer 4 Pfoten", "Michael Ende"); 
        Buch b3 = new Buch("Die 3 ???", "Robert Arthur"); 
        Buch b4 = new Buch("TKKG", "Stefan Wolf"); 
        Buch b5 = new Buch("Die 5 Freunde", "Enid Blyton"); 
 
        br.buchEinstellen(b1); 
        br.buchEinstellen(b2); 
        //br.buchEinstellen(b3); 
        //br.buchEinstellen(b4); 
        //br.buchEinstellen(b5);
        for (Buch b : br.regal) {
        	System.out.println(b==null ? b:b.titelAusgeben());
        }
        
 
        System.out.println("Before deletion: "+br.anzahlBuecher());
        br.buchMitTitelEntfernen("2 Freunde fuer 4 Pfoten"); 
        System.out.println("After deletion: "+br.anzahlBuecher());
 
    } 
}