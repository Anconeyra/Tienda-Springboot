package curso.java.tienda.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.draw.LineSeparator;

public class PDFHeaderFooter extends PdfPageEventHelper {
    //Template para el número total de páginas
    PdfTemplate total;
    
    //CABECERA
    //Evento que se ejecuta en cada nueva pagina del pdf
    public void onStartPage(PdfWriter writer, Document document) {
    	//Declaramos la imagen y texto de la cabecera
    	Phrase linea;
        Phrase imgCabecera;
        Phrase txtCabecera;
        Image imagen;
        Phrase txtFecha;
        
    	try {
	    	//Creamos un objeto PdfContentByte donde se guarda el contenido de una página en el pdf. Gráficos y texto.
	    	PdfContentByte cb = writer.getDirectContent();
		    	
	    	//imagen
    		//Obtenemos la imagen
    		//imagen = Image.getInstance("./media/logo-tienda.png");
    		//Convertimos la imagen a un Chunck (Chunck es la parte mas pequeña que puede ser añadida a un documento)
    		//Chunk chunk = new Chunk(imagen, 0, -60);   
    		//Convertimos el Chunk en un Phrase (Phrase es una serie de Chunks)
    		//imgCabecera = new Phrase(chunk);  
	    	//ColumnText.showTextAligned(cb, Element.ALIGN_CENTER, imgCabecera, document.rightMargin()+90, 
	    	//		document.top()+60, 0);
		    	
	    	//fecha
    		SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy");
    		String fecha = formateador.format(new Date());
    		txtFecha = new Phrase(fecha, new Font(FontFactory.getFont("Sans", 8, Font.NORMAL, BaseColor.BLACK)));
	    	ColumnText.showTextAligned(cb, Element.ALIGN_RIGHT, txtFecha, (document.right() - document.left()), document.top()+30, 0);
	    		    	
	    	//texto
    		//Agregamos un texto
    		txtCabecera = new Phrase("Factura simplificada");
	    	ColumnText.showTextAligned(cb, Element.ALIGN_RIGHT, txtCabecera, (document.right() - document.left()), document.top(), 0);
	      		    	
	    	//linea de arriba
	    	linea = new Phrase();
	    	linea.add(new LineSeparator(1, new Float(2.8), BaseColor.BLACK, Element.ALIGN_LEFT, 0));
	    	ColumnText.showTextAligned(cb, Element.ALIGN_LEFT, linea, document.rightMargin(), document.top()-5, 0);
	    	
	    	//linea de abajo
	    	linea = new Phrase();
	    	linea.add(new LineSeparator(1, new Float(2.8), BaseColor.BLACK, Element.ALIGN_LEFT, 0));
	    	ColumnText.showTextAligned(cb, Element.ALIGN_LEFT, linea, document.rightMargin(), document.top()-730, 0);
    	
	   		//Para dejar un margen debajo de la linea
			//document.add(new Paragraph(" "));
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
      
    }   

    //PIE
    public void onEndPage(PdfWriter writer, Document document) {
        //Creamos un objeto PdfContentByte donde se guarda el contenido 
        //de una página en el pdf. Gráficos y texto.
        PdfContentByte cb = writer.getDirectContent();
   
        //Asignamos el contenido al pie de página
        //getCurrentPageNumber() regresa la página actual
        Phrase pie = new Phrase(String.format("Página %d", writer.getCurrentPageNumber()));
   
        //Agregamos el pie a la página
        //con la siguiente nomenclaruta
        //ColumnText.showTextAligned(lienzo, alineacion, Phrase, posicion x, posicion y, rotacion);
        ColumnText.showTextAligned(cb, Element.ALIGN_CENTER, pie, (document.right() - document.left()) / 2 + document.leftMargin(), document.bottom()-20, 0);  
    } 
    
}

