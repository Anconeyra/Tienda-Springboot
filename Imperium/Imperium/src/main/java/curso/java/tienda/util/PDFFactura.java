package curso.java.tienda.util;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import curso.java.tienda.model.LineaPedido;
import curso.java.tienda.model.Pedido;
import curso.java.tienda.model.Producto;
import curso.java.tienda.model.Usuario;

public class PDFFactura {
	public static void crearFactura(Usuario usuario, Pedido pedido, ArrayList<LineaPedido> productosPedido){
		PdfWriter writer = null;
		Document documento = new Document(PageSize.A4, 20, 20, 70, 50);
		
		try {
			String numFactura = pedido.getNum_factura().toString();
			//
			writer = PdfWriter.getInstance(documento, new FileOutputStream("/Practicas/Curso Serbatic/Proyectos Spring Boot/TIENDA_ALBERTO_FERNANDEZ_RAMIREZ/src/main/resources/static/facturas/factura_" + numFactura + ".pdf"));
		
			writer.setPageEvent(new PDFHeaderFooter());
			
			documento.open();
			Paragraph paragraph = new Paragraph();
			String nombre = usuario.getNombreUsuario();
			String email = usuario.getEmail();
			paragraph.add("\n");
			paragraph.add("Datos del cliente:" + "\n");
			paragraph.add("Nombre: " + nombre  + "\n");
			paragraph.add("Email: " + email);
			paragraph.add("\n");
			paragraph.add("\n");
			float spacAfter = 5;
			paragraph.setSpacingAfter(spacAfter);
			documento.add(paragraph);
			
			
			Phrase texto = new Phrase("FACTURA DE PEDIDO");
			PdfPCell cabecera = new PdfPCell(texto);
			cabecera.setBackgroundColor(BaseColor.LIGHT_GRAY);
			cabecera.setBorderWidth(1);
			
			PdfPTable tabla = new PdfPTable(3);
			tabla.addCell("Producto");
		    tabla.addCell("Cantidad");
		    tabla.addCell("Precio");
			ArrayList<LineaPedido> carrito = productosPedido;
			float precioTotal = 0;
			float precioIva = 0;
	        if (carrito != null) {
	            for (LineaPedido producto : carrito) {
	            	tabla.addCell(producto.getProducto().getNombre());
	            	String cantidad = Integer.toString(producto.getProducto().getCantidad());
	            	tabla.addCell(cantidad);
	            	String precio = String.valueOf(producto.getPrecio_unidad());
	            	tabla.addCell(precio + "€");
	            	precioTotal += producto.getPrecio_unidad();
	            	precioIva += producto.getPrecio_unidad()*0.04;
	            }
	        }
	        
	        tabla.addCell("");
	        tabla.addCell("Envio");
	        tabla.addCell("Gratuito");
	        tabla.addCell("");
	        tabla.addCell("Subtotal");
	        tabla.addCell(String.valueOf(Math.round(precioTotal*100.00)/100.00) + "€");
	        tabla.addCell("");
	        tabla.addCell("IVA(4%)");
	        tabla.addCell(String.valueOf(Math.round(precioIva*100.00)/100.00) + "€");
	        tabla.addCell("");
	        tabla.addCell("Total");
	        tabla.addCell(String.valueOf(Math.round((precioTotal+precioIva)*100.00)/100.00) + "€");
		    documento.add(tabla);
		    
		    Paragraph paragraph2 = new Paragraph();
		    
		    paragraph2.add("\n");
		    paragraph2.add("\n");
		    paragraph2.add("Muchas gracias por su compra.");
		    documento.add(paragraph2);
		    
		    documento.close();
		    writer.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
