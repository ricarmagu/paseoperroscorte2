/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.paseoperros.controlador;

import co.edu.umanizales.listase.modelo.ListaDE;
import co.edu.umanizales.listase.modelo.NodoDE;
import co.edu.umanizales.listase.modelo.Perro;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import org.primefaces.model.diagram.Connection;
import org.primefaces.model.diagram.DefaultDiagramModel;
import org.primefaces.model.diagram.DiagramModel;
import org.primefaces.model.diagram.Element;
import org.primefaces.model.diagram.connector.FlowChartConnector;
import org.primefaces.model.diagram.endpoint.BlankEndPoint;
import org.primefaces.model.diagram.endpoint.EndPoint;
import org.primefaces.model.diagram.endpoint.EndPointAnchor;
import org.primefaces.model.diagram.overlay.ArrowOverlay;
import org.primefaces.model.diagram.overlay.LabelOverlay;

/**
 *
 * @author Nicolas Trujillo
 */
@Named(value = "listaDEController")
@SessionScoped
public class ListaDEController implements Serializable {
    
    private ListaDE listaPerrosDE;   
    private Perro perroMostrar;
    private NodoDE tempDE;
    private int datobuscar;
    private Perro perroEncontrado;
    private DefaultDiagramModel model;

    private int seleccionUbicacion=0;
    private int totalPerros = 0;
    /**
     * Creates a new instance of ListaDEController
     */
    public ListaDEController() {
    }
    
    @PostConstruct
    public void iniciar() {
        
        listaPerrosDE = new ListaDE();
//        // Conectaría a un archivo plano o a una base de datos para llenar la
//        Perro perro1 = new Perro("Pastor", (byte) 1, (byte) 3, "Masculino" );
//        //lista de perros
//        this.getListaPerros().adicionarAlFinalNodoDE(perro1);
////        listaPerrosDE.adicionarAlFinalNodoDE(perro1);
//        listaPerrosDE.adicionarNodo(new Perro("Lulú", (byte) 2, (byte) 4, "Femenino"));
//        listaPerrosDE.adicionarNodo(new Perro("Firulais", (byte) 3, (byte) 6, "Masculino"));
//
//        listaPerrosDE.adicionarNodo(new Perro("Rocky", (byte) 4, (byte) 5, "Masculino"));
//        perroMostrar = listaPerrosDE.getCabeza().getDato();
        tempDE = listaPerrosDE.getCabeza();

        inicializarModelo();
    }

    public int getSeleccionUbicacion() {
        return seleccionUbicacion;
    }

    public void setSeleccionUbicacion(int seleccionUbicacion) {
        this.seleccionUbicacion = seleccionUbicacion;
    }
    
    public int getDatobuscar() {
        return datobuscar;
    }

    public void setDatobuscar(int datobuscar) {
        this.datobuscar = datobuscar;
    }

    public Perro getPerroEncontrado() {
        return perroEncontrado;
    }

    public void setPerroEncontrado(Perro perroEncontrado) {
        this.perroEncontrado = perroEncontrado;
    }

    public NodoDE getTempDE() {
        return tempDE;
    }

    public void setTempDE(NodoDE tempDE) {
        this.tempDE = tempDE;
    }

    public Perro getPerroMostrar() {
        return perroMostrar;
    }

    public void setPerroMostrar(Perro perroMostrar) {
        this.perroMostrar = perroMostrar;
    }

    public ListaDE getListaPerros() {
        return listaPerrosDE;
    }

    public void setListaPerros(ListaDE listaPerrosDE) {
        this.listaPerrosDE = listaPerrosDE;
    }

    public int getTotalPerros() {
        return totalPerros;
    }

    public void setTotalPerros(int totalPerros) {
        this.totalPerros = totalPerros;
    }

    public ListaDE getListaPerrosDE() {
        return listaPerrosDE;
    }

    public void setListaPerrosDE(ListaDE listaPerrosDE) {
        this.listaPerrosDE = listaPerrosDE;
    }
 
    public void irSiguiente() {
        if(tempDE.getSiguiente()!=null)
        {
        tempDE = tempDE.getSiguiente();
        perroMostrar = tempDE.getDato();
        }
    }

    public void irPrimero() {
        if (listaPerrosDE.getCabeza() != null) {
            tempDE = listaPerrosDE.getCabeza();
            perroMostrar = tempDE.getDato();
            inicializarModelo();
        }
        else
        {
            JsfUtil.addErrorMessage("No existen datos en la lista");
        }

    }

    public void irUltimo() {

        tempDE = listaPerrosDE.getCabeza();
        while (tempDE.getSiguiente() != null) {
            tempDE = tempDE.getSiguiente();
        }
        /// Parado en el último nodo
        perroMostrar = tempDE.getDato();
    }

    public void invertir() {
        listaPerrosDE.invertir();
        irPrimero();
        inicializarModelo();
    }

    public void intercambiar() {
        listaPerrosDE.intercambiarExtremos();
        irPrimero();
    }

    public void eliminar() {
        listaPerrosDE.eliminarPorPosicion(tempDE.getDato().getNumero());
        irPrimero();
    }

    public void buscarPerro(int datobuscar) {
        perroEncontrado = listaPerrosDE.encontrarxPosicionDE(datobuscar).getDato();
    }

    public void inicializarModelo() {
        //Instanciar el modelo
        model = new DefaultDiagramModel();
        //Definirle al modelo la contidad de enlaces -1 (infinito)
        model.setMaxConnections(-1);

        FlowChartConnector connector = new FlowChartConnector();
        connector.setPaintStyle("{strokeStyle:'#C7B097',lineWidth:3}");
        model.setDefaultConnector(connector);

        //pregunto si hay datos
        if (listaPerrosDE.getCabeza() != null) {
            //Llamo a mi ayudante y lo ubico en el primero
            NodoDE ayudante = listaPerrosDE.getCabeza();
            //recorro mientras el ayudante tenga datos
            int posX = 2;
            int posY = 2;
            while (ayudante != null) {
                Element perroPintar = new Element(ayudante.getDato().getNombre(), posX + "em", posY + "em");

                if (ayudante.getDato().getNombre().toLowerCase().startsWith("r")) {
                    perroPintar.setStyleClass("ui-diagram-success");
                }

                perroPintar.addEndPoint(new BlankEndPoint(EndPointAnchor.CONTINUOUS_RIGHT));
                perroPintar.addEndPoint(new BlankEndPoint(EndPointAnchor.TOP));
                model.addElement(perroPintar);
                ayudante = ayudante.getSiguiente();
                posX = posX + 5;
                posY = posY + 5;
            }

            // el ayudante quedo en el enlace del último
            //Ya pinte todos los elementos y los puntos de enlace
            for (int i = 0; i < model.getElements().size() - 1; i++) {
                model.connect(createConnection(model.getElements().get(i).getEndPoints().get(0),
                        model.getElements().get(i + 1).getEndPoints().get(1), null));
            }
        }
    }

    public DiagramModel getModel() {
        return model;
    }

    private Connection createConnection(EndPoint from, EndPoint to, String label) {
        Connection conn = new Connection(from, to);
        conn.getOverlays().add(new ArrowOverlay(20, 20, 1, 1));

        if (label != null) {
            conn.getOverlays().add(new LabelOverlay(label, "flow-label", 0.5));
        }

        return conn;
    }

    public void guardarPerro() {
        switch(seleccionUbicacion)
        {
            case 1:
                listaPerrosDE.adicionarAlInicio(perroEncontrado) ;
                break;
            case 2:
                listaPerrosDE.adicionarNodo(perroEncontrado);
                break;
            default: listaPerrosDE.adicionarNodo(perroEncontrado);
        }
        
        perroEncontrado = new Perro();
        irPrimero();
        JsfUtil.addSuccessMessage("Se ha adicionado el perro a la lista");
        setTotalPerros(this.totalPerros + 1);
    }
    

    public String listade() {
        perroEncontrado = new Perro();
        //pinta
        inicializarModelo();
        return "listade";
    }
    public String irCrearPerroDE() {
        perroEncontrado = new Perro();
        
        return "crearDE";
    }
    
}

