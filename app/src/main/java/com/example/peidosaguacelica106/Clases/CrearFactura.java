package com.example.peidosaguacelica106.Clases;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.example.peidosaguacelica106.Datos.Datos;
import com.example.peidosaguacelica106.R;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.io.source.ByteArrayOutputStream;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.property.TextAlignment;


import org.abego.treelayout.internal.util.java.lang.string.StringUtil;



import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CrearFactura {


    public void createPDF(String pdfname,String pdffecha,Context context, ArrayList<Datos> lista,int iten) throws FileNotFoundException {

        Toast.makeText(context,
                pdfname,
                Toast.LENGTH_LONG).show();

        String pdf_path= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();

        File file=new File(pdf_path,
                pdfname);
        OutputStream outputStream=new FileOutputStream(file);
        PdfWriter pdfWriter=new PdfWriter(file);
        PdfDocument pdfDocument=new PdfDocument(pdfWriter);
        Document document=new Document(pdfDocument);
       // document.setMargins(0,0,0,0);

        Drawable d1=context.getDrawable(R.drawable.fondo);
        Bitmap bitmap=((BitmapDrawable)d1).getBitmap();
        ByteArrayOutputStream stream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);
        byte[]bitemapData1=stream.toByteArray();

        ImageData imageData1= ImageDataFactory.create(bitemapData1);
        Image image1=new Image(imageData1);

        document.add(image1.setFixedPosition(0,0).setWidth(600).setHeight(850));
        //document.add(image1.setFixedPosition(0,0));
        document.setMargins(100,100,100,100);


        float columnWith[]={112,112,120,104,112};
        Table table=new Table(columnWith);

        //fila 1
        table.addCell(new Cell(1,5).add(new Paragraph("AGUA PURIFICADA CELICA").setFontSize(20f).setBold()).setFontColor(ColorConstants.WHITE).setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER) );
        //table.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER);
        //table.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
        //table.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
        //table.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));

        //fila 2
        table.addCell(new Cell(1,5).add(new Paragraph("Salud y Pureza Celestial\n"+"Calle Jose Cuero y Caicedo y Eloy Alfaro \n" +
                "Celica - Loja - Ecuador"+ "")).setFontColor(ColorConstants.BLACK).setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER));
        //table.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
        //table.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
        //table.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
        //table.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));

        //fila 3
        table.addCell(new Cell().add(new Paragraph("\n")).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));





        Text textbilletto=new Text("Datos del cliente \n");
        textbilletto.setBold();
        textbilletto.setFontColor(new DeviceRgb(18,192,33));
        Paragraph paragraphbilletto=new Paragraph();
        paragraphbilletto.add(textbilletto);
        paragraphbilletto.add(  "Nombre: "+lista.get(iten).getNombre()+"\n"+
                "Direccion:"+lista.get(iten).getDireccion()+"\n"+
                "Telefono:"+lista.get(iten).getCelular()+" \n"+
                "Ciudad:"+"Celica-Loja"+"\n"+"\n"+"\n"+"\n"+"\n"+"\n");
        table.addCell(new Cell(1,2).add(paragraphbilletto).setFontColor(ColorConstants.BLACK).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
        //table.addCell(new Cell().add(paragraphfactura).setBorder(Border.NO_BORDER));


        //numero de factura
        Text textnumerofactura=new Text("");
        textnumerofactura.setBold();
        textnumerofactura.setFontColor(ColorConstants.WHITE);
        Paragraph paragraphfactura=new Paragraph();
        paragraphfactura.add(textnumerofactura);
        paragraphfactura.add(pdfname);
        //fechade factura
        Text textfecha=new Text("FECHA DEL RECIBO: \n");
        textfecha.setBold();
        textfecha.setFontColor(ColorConstants.WHITE);
        Paragraph paragraphfecha=new Paragraph();

        paragraphfecha.add(textfecha);
        String fecha=pdffecha.substring(0,2)+":"+pdffecha.substring(2,4)+":"+pdffecha.substring(4,6)+" "+pdffecha.substring(7,10)+"/"+pdffecha.substring(10,12)+"/"+pdffecha.substring(12,16)+" ";
        paragraphfecha.add(fecha);





        //fila 5
        table.addCell(new Cell().add(new Paragraph("RECIBO")).setBorder(Border.NO_BORDER).setBold().setFontSize(24).setFontColor(new DeviceRgb(18,192,33)).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(paragraphfactura).setFontColor(ColorConstants.WHITE).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(paragraphfecha).setFontColor(ColorConstants.WHITE).setBorder(Border.NO_BORDER));
        //fila 5
        table.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
        //fila 6

        table.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(new Paragraph("Descripcion").setFontColor(ColorConstants.WHITE)).setBackgroundColor(new DeviceRgb(66,133,244)));
        table.addCell(new Cell().add(new Paragraph("Cantidad").setFontColor(ColorConstants.WHITE)).setBackgroundColor(new DeviceRgb(66,133,244)));
        table.addCell(new Cell().add(new Paragraph("Valor unitario").setFontColor(ColorConstants.WHITE)).setBackgroundColor(new DeviceRgb(66,133,244)));
        table.addCell(new Cell().add(new Paragraph("Total").setFontColor(ColorConstants.WHITE)).setBackgroundColor(new DeviceRgb(66,133,244)));



        PedidosyVendidos pedidos=new PedidosyVendidos();
        String aux=lista.get(iten).getPedidos();
        String []calcular_factura=pedidos.obtener_botellones(aux);
        String []items=pedidos.pedidos_factura(calcular_factura);
        String a="";
        String []items1=new String[22];
        int j=0;
        for(int i=0;i<items.length ;i++){
            //a=a+items[i]+"\n";
            if(!TextUtils.equals(items[i],"0")) {
                items1[j] = items[i];
                j++;
                a=a+items1[j]+"\n";
            }
        }
        int limite_pedido= Integer.parseInt(items[21]);
        for(int i=0;i<limite_pedido; i++) {
                table.addCell(new Cell( ).add(new Paragraph("")).setBorder(Border.NO_BORDER));
                table.addCell(new Cell().add(new Paragraph(items1[(i*4)])).setBackgroundColor(new DeviceRgb(220, 220, 220)));
                table.addCell(new Cell().add(new Paragraph(items1[(1)+(i*4)])).setBackgroundColor(new DeviceRgb(220, 220, 220)));
                table.addCell(new Cell().add(new Paragraph(items1[(2)+(i*4)])).setBackgroundColor(new DeviceRgb(220, 220, 220)));
                table.addCell(new Cell().add(new Paragraph(items1[(3)+(i*4)])).setBackgroundColor(new DeviceRgb(220, 220, 220)));

              }
        //fila 8
/*
        table.addCell(new Cell().add(new Paragraph(a)).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(new Paragraph("ITEN")).setBackgroundColor(new DeviceRgb(220,220,220)));
        table.addCell(new Cell().add(new Paragraph("100")).setBackgroundColor(new DeviceRgb(220,220,220)));
        table.addCell(new Cell().add(new Paragraph("1")).setBackgroundColor(new DeviceRgb(220,220,220)));
        table.addCell(new Cell().add(new Paragraph("100")).setBackgroundColor(new DeviceRgb(220,220,220)));

*/




        //fila 11
        table.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));

        //fila 12
        table.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(new Paragraph("SUBTOTAL")).setFontColor(ColorConstants.WHITE));
        table.addCell(new Cell().add(new Paragraph(calcular_factura[12])).setFontColor(ColorConstants.WHITE));

        //fila 13
        table.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(new Paragraph("ENVIO")).setFontColor(ColorConstants.WHITE));
        table.addCell(new Cell().add(new Paragraph("0.50")).setFontColor(ColorConstants.WHITE));

        //fila 14
        table.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(new Paragraph("I.V.A.")).setFontColor(ColorConstants.WHITE));
        table.addCell(new Cell().add(new Paragraph("0%")).setFontColor(ColorConstants.WHITE));

        //fila 15
        table.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(new Paragraph("DESCUENTO")).setFontColor(ColorConstants.WHITE));
        DecimalFormat df=new DecimalFormat("#.00");
        String iva=df.format(Float.parseFloat(calcular_factura[12])*0.12);
        //table.addCell(new Cell().add(new Paragraph(iva)).setFontColor(ColorConstants.WHITE));
        table.addCell(new Cell().add(new Paragraph("0.50")).setFontColor(ColorConstants.WHITE));

        //fila 16
        table.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
        table.addCell(new Cell(1,2).add(new Paragraph("=======").setTextAlignment(TextAlignment.CENTER)).setBorder(Border.NO_BORDER));
        //table.addCell(new Cell().add(new Paragraph("")));

        //fila 17
        table.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));

        Text texttotal=new Text("TOTAL \n");
        texttotal.setFontColor(ColorConstants.WHITE);
        Paragraph paragraphtotal=new Paragraph();
        paragraphtotal.add(texttotal);
        paragraphtotal.add(calcular_factura[12]);
        table.addCell(new Cell(1,2  ).add(paragraphtotal).setFontSize(24).setTextAlignment(TextAlignment.CENTER).setFontColor(ColorConstants.WHITE));
        //table.addCell(new Cell().add(new Paragraph("")));


        //fila 18
        table.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));


        document.add(table);
        document.add(new Paragraph("Xavier Ramon\nFirma Autorizada").setFontColor(ColorConstants.WHITE));
        document.close();
        Toast.makeText(context,"pdf creado satisfactoriamente",Toast.LENGTH_LONG).show();
   }
    public static String getLastN(String s, int n) {
        if (s == null || n > s.length()) {
            return s;
        }
        s.substring(s.length() - n);
        String g=s.substring(14);
        return g;
    }
}
