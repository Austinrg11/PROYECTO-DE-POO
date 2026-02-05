/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gimnasio;

/**
 *
 * @author melvi
 */
import java.awt.BorderLayout;
import java.awt.Image;
import java.sql.Date;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.SwingUtilities;

import javax.swing.JOptionPane;

public class Frm_Menu_A extends javax.swing.JFrame {

    //CLASE DE LAS CONEXIONES/////
    ClienteDAO cli;
    EntrenadorDAO entre;
    EquipoDAO eq;
    PlanDAO plan;
    AsistenciaDAO asis;
    Cliente_PlanDAO cp;
    Uso_EquipoDAO uso;

    /**
     * Creates new form Frm_Menu_A
     */
    public Frm_Menu_A() {
        initComponents();
        this.setLocationRelativeTo(null);

        try {
            // DAOs
            cli = new ClienteDAO();
            entre = new EntrenadorDAO();
            eq = new EquipoDAO();
            plan = new PlanDAO();
            asis = new AsistenciaDAO();
            cp = new Cliente_PlanDAO();
            uso = new Uso_EquipoDAO();

            // ===== CLIENTES =====
            cargarTabla("");
            cargarComboClientes();
            limpiarC();

            // ===== ENTRENADORES =====
            limpiarTablaEntrenador();
            cargarTablaEntrenador("");
            cargarComboEntrenador();
            limpiarE();

            // ===== EQUIPOS =====
            cargarTablaEquipo("");
            CMBESTADO_EQUIPO1.removeAllItems();
            CMBESTADO_EQUIPO1.addItem("ACTIVO");
            CMBESTADO_EQUIPO1.addItem("INACTIVO");

            // ===== PLANES =====
            cargarTablaPlan("");
            cargarComboCosto();
            cargarComboDuracion();
            limpiarP();

            // ===== ASISTENCIA =====
            llenarHoras();
            cargarTablaAsistencia("");
            limpiarA();

            // ===== PLANES DE CLIENTES =====
            cargarTablaCP("");
            cargarComboCliente();
            cargarComboPlan();
            CMBESTADO.removeAllItems();
            CMBESTADO.addItem("ACTIVO");
            CMBESTADO.addItem("INACTIVO");
            limpiarPC();

            // ===== USO DE EQUIPOS =====
            cargarComboClientes();
            cargarComboEquipos();
            cargarComboTiempo();
            cargarTablaUsoEquipo("");

            limpiarU();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error general al iniciar: " + e);
        }
    }

    //TABLA DE CLIENTES//
    private void cargarTabla(String filtro) {
        DefaultTableModel modelo = new DefaultTableModel();
        modelo.addColumn("ID");
        modelo.addColumn("Nombres");
        modelo.addColumn("Apellidos");
        modelo.addColumn("Celular");
        modelo.addColumn("Direccion");
        modelo.addColumn("Correo");
        modelo.addColumn("Fecha");

        try {
            ResultSet rs;
            if (filtro.equals("")) {
                rs = cli.bd.ConsultaBD("SELECT * FROM cliente");
            } else {
                rs = cli.bd.ConsultaBD("SELECT * FROM cliente WHERE NOMBRES LIKE '%" + filtro + "%' OR APELLIDOS LIKE '%" + filtro + "%'");
            }

            while (rs.next()) {
                modelo.addRow(new Object[]{
                    rs.getInt("IDCLIENTE"),
                    rs.getString("NOMBRES"),
                    rs.getString("APELLIDOS"),
                    rs.getString("CELULAR"),
                    rs.getString("DIRECCION"),
                    rs.getString("CORREO"),
                    rs.getDate("FECHAREGISTRO")
                });
            }

            TABLACLIENTE.setModel(modelo);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar tabla: " + e);
        }
    }

    private void limpiarC() {
        TXTNOMBRESCLIENTES.setText("");
        TXTAPELLIDOSCLIENTE.setText("");
        TXTCELULARCLIENTES.setText("");
        TXTDIRECCIONCLIENTES.setText("");
        TXTCORREO.setText("");
        CALENDARIOCLIENTE.setDate(null);
    }
    //-------------------------------//

    //TABLA ENTRENADOR////
    private void cargarTablaEntrenador(String filtro) {
        DefaultTableModel modelo = new DefaultTableModel();
        modelo.addColumn("ID");
        modelo.addColumn("Nombres");
        modelo.addColumn("Especialidad");
        modelo.addColumn("Celular");

        try {
            ResultSet rs;
            if (filtro.equals("")) {
                rs = entre.bd.ConsultaBD("SELECT * FROM entrenador");
            } else {
                rs = entre.bd.ConsultaBD(
                        "SELECT * FROM entrenador WHERE NOMBRES LIKE '%" + filtro + "%' OR ESPECIALIDAD LIKE '%" + filtro + "%'"
                );
            }

            while (rs.next()) {
                modelo.addRow(new Object[]{
                    rs.getInt("IDENTRENADOR"),
                    rs.getString("NOMBRES"),
                    rs.getString("ESPECIALIDAD"),
                    rs.getString("CELULAR")
                });
            }

            TABLAENTRENADOR.setModel(modelo);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar tabla entrenador: " + e);
        }
    }

    private void limpiarENTRE() {
        TXTNOMBRETRADOR.setText("");
        TXTESPECIALIDAD.setText("");
        TXTCELULAR.setText("");

    }

    //----------------------------//
    //TABLA EQUIPOS, COMBOBOX.//
    public void cargarTablaEquipo(String dato) {
        DefaultTableModel modelo = new DefaultTableModel();
        modelo.addColumn("ID");
        modelo.addColumn("ID ENTRENADOR");
        modelo.addColumn("ENTRENADOR");
        modelo.addColumn("EQUIPO");
        modelo.addColumn("TIPO");
        modelo.addColumn("ESTADO");
        modelo.addColumn("DESCRIPCIÓN");

        TABLAEQUIPO.setModel(modelo);

        try {
            ResultSet rs = eq.ConsultarEquipo(dato);

            while (rs.next()) {
                modelo.addRow(new Object[]{
                    rs.getInt("IDEQUIPO"),
                    rs.getInt("IDENTRENADOR"),
                    rs.getString("ENTRENADOR"),
                    rs.getString("NOMBREEQUIPO"),
                    rs.getString("TIPO"),
                    rs.getString("ESTADO"),
                    rs.getString("DESCRIPCION")
                });
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error cargar tabla equipo: " + e);
        }
    }

    public void cargarComboEntrenador() {
        try {
            ResultSet rs = entre.bd.ConsultaBD("SELECT IDENTRENADOR, NOMBRES FROM entrenador");
            CMBENTRENADOR.removeAllItems();

            while (rs.next()) {
                CMBENTRENADOR.addItem(rs.getInt("IDENTRENADOR") + " - " + rs.getString("NOMBRES"));
            }

        } catch (Exception e) {
            System.out.println("Error al cargar combo entrenador: " + e);
        }
    }

    private void seleccionarItemCombo(JComboBox combo, String valor) {
        for (int i = 0; i < combo.getItemCount(); i++) {
            String item = combo.getItemAt(i).toString();

            if (item.startsWith(valor + " -")) {
                combo.setSelectedIndex(i);
                return;
            }
        }
    }

    private void limpiarE() {
        TXTEQUIPO.setText("");
        CMBENTRENADOR.setSelectedIndex(-1);
        CMBESTADO_EQUIPO1.setSelectedIndex(-1);
        TXTIPO.setText("");
        TXTNAMEEQUIPO.setText("");
        TXTDESCRIPCION.setText("");
    }
    //-------------------///

    //TABLAS,COMBOBOX PLANES////
    private void cargarComboCosto() {
        CMBCOSTO.removeAllItems();
        CMBCOSTO.addItem("10$");
        CMBCOSTO.addItem("20$");
        CMBCOSTO.addItem("30$");
        CMBCOSTO.addItem("40$");
        CMBCOSTO.addItem("50$");
    }

    private void cargarComboDuracion() {
        CMBDURACION.removeAllItems();
        CMBDURACION.addItem("DIARIO");
        CMBDURACION.addItem("SEMANAL");
        CMBDURACION.addItem("MENSUAL");
        CMBDURACION.addItem("3 MESES");
        CMBDURACION.addItem("6 MESES");
        CMBDURACION.addItem("ANUAL");
    }

    private double obtenerCosto() {
        String costoStr = CMBCOSTO.getSelectedItem().toString().replace("$", "");
        return Double.parseDouble(costoStr);
    }

    private int obtenerDuracion() {
        String op = CMBDURACION.getSelectedItem().toString();

        switch (op) {
            case "DIARIO":
                return 1;
            case "SEMANAL":
                return 7;
            case "MENSUAL":
                return 30;
            case "3 MESES":
                return 90;
            case "6 MESES":
                return 180;
            case "ANUAL":
                return 365;
        }
        return 0;
    }

    private void seleccionarDuracionCombo(int dias) {
        if (dias == 1) {
            CMBDURACION.setSelectedItem("DIARIO");
        } else if (dias == 7) {
            CMBDURACION.setSelectedItem("SEMANAL");
        } else if (dias == 30) {
            CMBDURACION.setSelectedItem("MENSUAL");
        } else if (dias == 90) {
            CMBDURACION.setSelectedItem("3 MESES");
        } else if (dias == 180) {
            CMBDURACION.setSelectedItem("6 MESES");
        } else if (dias == 365) {
            CMBDURACION.setSelectedItem("ANUAL");
        }
    }

    private void cargarTablaPlan(String busqueda) {
        DefaultTableModel modelo = new DefaultTableModel();
        modelo.addColumn("ID");
        modelo.addColumn("Nombre");
        modelo.addColumn("Costo");
        modelo.addColumn("Duración");
        modelo.addColumn("Descripción");

        try {
            ResultSet rs = plan.ConsultarPlan(busqueda);

            while (rs.next()) {
                modelo.addRow(new Object[]{
                    rs.getInt("IDPLAN"),
                    rs.getString("NOMBREPLAN"),
                    rs.getDouble("COSTO"),
                    rs.getInt("DURACION"),
                    rs.getString("DESCRIPCION")
                });
            }

            TABLAPLAN.setModel(modelo);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar tabla: " + e);
        }
    }

    private void limpiarP() {
        TXTIDPLAN.setText("");
        TXTNOMBREPLAN.setText("");
        TXDESCRIPCION.setText("");
        CMBCOSTO.setSelectedIndex(-1);
        CMBDURACION.setSelectedIndex(-1);
    }

    private void seleccionarItemCombox(javax.swing.JComboBox combo, String valor) {
        for (int i = 0; i < combo.getItemCount(); i++) {
            if (combo.getItemAt(i).toString().equalsIgnoreCase(valor)) {
                combo.setSelectedIndex(i);
                break;
            }
        }
    }

    //----METODO ASISTENCIA----//
    public void llenarHoras() {
        CMBHORAENTRADA.removeAllItems();
        CMBHORASALIDA.removeAllItems();
        for (int i = 8; i <= 22; i++) {
            String hora = (i < 10 ? "0" + i : i) + ":00:00";
            CMBHORAENTRADA.addItem(hora);
            CMBHORASALIDA.addItem(hora);
        }
    }

    public void cargarTablaAsistencia(String filtro) {
        DefaultTableModel modelo = new DefaultTableModel();
        modelo.addColumn("ID");
        modelo.addColumn("ID CLIENTE");
        modelo.addColumn("CLIENTE");
        modelo.addColumn("FECHA");
        modelo.addColumn("HORAENTRADA");
        modelo.addColumn("HORASALIDA");

        try {
            ResultSet rs = asis.ConsultarAsistencia(filtro);

            int contador = 0;
            while (rs.next()) {
                // Obtener valores
                Object idAsistencia = rs.getObject("IDASISTENCIA");
                int idAsist = (idAsistencia != null && rs.getInt("IDASISTENCIA") > 0) ? rs.getInt("IDASISTENCIA") : 0;

                // La fecha SIEMPRE tendrá valor (asistencia o registro del cliente)
                java.sql.Date fecha = rs.getDate("FECHA");

                // Horas pueden ser NULL
                Object horaEntrada = rs.getObject("HORAENTRADA");
                Object horaSalida = rs.getObject("HORASALIDA");

                modelo.addRow(new Object[]{
                    idAsist > 0 ? idAsist : "", // ID asistencia vacío si no existe
                    rs.getInt("IDCLIENTE"),
                    rs.getString("CLIENTE"),
                    fecha, // Fecha SIEMPRE presente
                    horaEntrada != null ? rs.getTime("HORAENTRADA") : "",
                    horaSalida != null ? rs.getTime("HORASALIDA") : ""
                });
                contador++;
            }

            TABLASISTENCIA.setModel(modelo);
            System.out.println("✓ Se cargaron " + contador + " clientes en la tabla");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar asistencias: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void limpiarA() {
        TXTCLIENTE.setText("");
        FECHASISTENCIAA.setDate(null);
        CMBHORAENTRADA.setSelectedIndex(-1);
        CMBHORASALIDA.setSelectedIndex(-1);
    }

    //METODOS CLIENTE PLAN////
    public void cargarTablaCP(String filtro) {
        DefaultTableModel modelo = new DefaultTableModel();
        modelo.addColumn("ID");
        modelo.addColumn("ID CLIENTE");
        modelo.addColumn("CLIENTE");
        modelo.addColumn("ID PLAN");
        modelo.addColumn("PLAN");
        modelo.addColumn("INICIO");
        modelo.addColumn("FIN");
        modelo.addColumn("ESTADO");

        try {
            ResultSet rs = cp.ConsultarClientePlan(filtro);
            while (rs.next()) {
                modelo.addRow(new Object[]{
                    rs.getInt("IDCLIENTEPLAN"),
                    rs.getInt("IDCLIENTE"),
                    rs.getString("CLIENTE"),
                    rs.getInt("IDPLAN"),
                    rs.getString("PLAN"),
                    rs.getDate("FECHAINICIO"),
                    rs.getDate("FECHAFIN"),
                    rs.getString("ESTADO")
                });
            }
            TABLA_CLIENTE_PLAN.setModel(modelo);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error cliente-plan: " + e);
        }
    }

    private void cargarComboCliente() {
        try {
            CMBCLIENTE.removeAllItems();
            ResultSet rs = cli.bd.ConsultaBD("SELECT IDCLIENTE, NOMBRES, APELLIDOS FROM cliente");
            while (rs.next()) {
                CMBCLIENTE.addItem(
                        rs.getInt("IDCLIENTE") + " - "
                        + rs.getString("NOMBRES") + " "
                        + rs.getString("APELLIDOS")
                );
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar clientes: " + e);
        }
    }

    private void cargarComboPlan() {
        try {
            CMBPLAN.removeAllItems();
            ResultSet rs = plan.bd.ConsultaBD("SELECT IDPLAN, NOMBREPLAN FROM plan");
            while (rs.next()) {
                CMBPLAN.addItem(
                        rs.getInt("IDPLAN") + " - "
                        + rs.getString("NOMBREPLAN")
                );
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar planes: " + e);
        }
    }

    private int obtenerIdCliente() {
        String valor = CMBCLIENTE.getSelectedItem().toString();
        return Integer.parseInt(valor.split(" - ")[0]);
    }

    private int obtenerIdPlan() {
        String valor = CMBPLAN.getSelectedItem().toString();
        return Integer.parseInt(valor.split(" - ")[0]);
    }

    private void seleccionarItemComboxpc(JComboBox<String> combo, String valor) {
        for (int i = 0; i < combo.getItemCount(); i++) {
            if (combo.getItemAt(i).startsWith(valor + " -")) {
                combo.setSelectedIndex(i);
                break;
            }
        }
    }

    private void limpiarPC() {
        TXTIDCLIENTE_PLAN.setText("");
        CMBCLIENTE.setSelectedIndex(-1);
        CMBESTADO.setSelectedIndex(-1);
        CMBPLAN.setSelectedIndex(-1);
        FECHAINICIO.setDate(null);
        FECHAFINAL.setDate(null);
    }

    // METODOS DE USO DE EQUIPOS//
    public String formatearTiempoUsoEquipo(int minutos) {
        if (minutos <= 0) {
            return "0 min";
        }

        // Si es menos de 1 hora, mostrar en minutos
        if (minutos < 60) {
            return minutos + " min";
        }

        // Si es menos de 1 día (24 horas), mostrar en horas
        if (minutos < 1440) { // 1440 = 24 * 60
            int horas = minutos / 60;
            int mins = minutos % 60;

            if (mins > 0) {
                return horas + "h " + mins + "min";
            }
            return horas + "h";
        }

        // Si es más de 1 día, mostrar en días
        int dias = minutos / 1440;
        int horasRestantes = (minutos % 1440) / 60;

        // Si es más de 30 días, mostrar en meses y días
        if (dias >= 30) {
            int meses = dias / 30;
            int diasRestantes = dias % 30;

            if (diasRestantes > 0) {
                return meses + " mes" + (meses > 1 ? "es" : "") + " " + diasRestantes + " día" + (diasRestantes > 1 ? "s" : "");
            }
            return meses + " mes" + (meses > 1 ? "es" : "");
        }

        // Mostrar en días
        if (horasRestantes > 0) {
            return dias + " día" + (dias > 1 ? "s" : "") + " " + horasRestantes + "h";
        }
        return dias + " día" + (dias > 1 ? "s" : "");
    }

    private int convertirTextoAMinutos(String texto) {
        try {
            // Casos: "30 min", "2h", "2h 30min", "5 días", "3 meses", etc.
            int totalMinutos = 0;

            // Meses
            if (texto.contains("mes")) {
                String[] partes = texto.split("mes");
                int meses = Integer.parseInt(partes[0].trim());
                totalMinutos += meses * 30 * 24 * 60; // 30 días por mes

                if (partes.length > 1 && partes[1].contains("día")) {
                    String dias = partes[1].replaceAll("[^0-9]", "").trim();
                    if (!dias.isEmpty()) {
                        totalMinutos += Integer.parseInt(dias) * 24 * 60;
                    }
                }
                return totalMinutos;
            }

            // Días
            if (texto.contains("día")) {
                String[] partes = texto.split("día");
                int dias = Integer.parseInt(partes[0].trim());
                totalMinutos += dias * 24 * 60;

                if (partes.length > 1 && partes[1].contains("h")) {
                    String horas = partes[1].replaceAll("[^0-9]", "").trim();
                    if (!horas.isEmpty()) {
                        totalMinutos += Integer.parseInt(horas) * 60;
                    }
                }
                return totalMinutos;
            }

            // Horas
            if (texto.contains("h")) {
                String[] partes = texto.split("h");
                int horas = Integer.parseInt(partes[0].trim());
                totalMinutos += horas * 60;

                if (partes.length > 1 && partes[1].contains("min")) {
                    String mins = partes[1].replaceAll("[^0-9]", "").trim();
                    if (!mins.isEmpty()) {
                        totalMinutos += Integer.parseInt(mins);
                    }
                }
                return totalMinutos;
            }

            // Solo minutos
            if (texto.contains("min")) {
                String mins = texto.replaceAll("[^0-9]", "").trim();
                return Integer.parseInt(mins);
            }

        } catch (Exception e) {
            System.out.println("Error al convertir texto a minutos: " + e);
        }

        return 0;
    }

    public String formatMinutosToLabel(int minutos) {
        if (minutos <= 0) {
            return "0M";
        }

        int h = minutos / 60;
        int m = minutos % 60;

        if (h > 0 && m > 0) {
            return h + "H" + m + "M";
        }
        if (h > 0) {
            return h + "H";
        }
        return m + "M";
    }

    public int convertirTiempoAMinutos(String tiempo) {
        if (tiempo == null || tiempo.trim().isEmpty()) {
            return 0;
        }

        tiempo = tiempo.toUpperCase().trim();
        int minutos = 0;

        try {
            if (tiempo.contains("H")) {
                String[] partes = tiempo.split("H");
                int horas = Integer.parseInt(partes[0].replaceAll("[^0-9]", ""));
                minutos += horas * 60;

                if (partes.length > 1 && partes[1].contains("M")) {
                    int mins = Integer.parseInt(partes[1].replaceAll("[^0-9]", ""));
                    minutos += mins;
                }
            } else if (tiempo.contains("M")) {
                minutos = Integer.parseInt(tiempo.replaceAll("[^0-9]", ""));
            } else {
                minutos = Integer.parseInt(tiempo); // solo número
            }
        } catch (Exception e) {
            System.out.println("Error convertirTiempoAMinutos: " + e);
        }

        return minutos;
    }

    private void cargarComboClientes() {
        try {

            CMBCLIENTE.removeAllItems();
            CMBCLIENTE1.removeAllItems();

            ResultSet rs = cli.bd.ConsultaBD("SELECT IDCLIENTE, NOMBRES, APELLIDOS FROM cliente");
            while (rs.next()) {
                int id = rs.getInt("IDCLIENTE");
                String nombre = rs.getString("NOMBRES");
                String ap = rs.getString("APELLIDOS");
                String item = id + " - " + nombre + " " + ap;

                CMBCLIENTE.addItem(item);
                CMBCLIENTE1.addItem(item);
            }
            CMBCLIENTE.setSelectedIndex(-1);
            CMBCLIENTE1.setSelectedIndex(-1);
        } catch (Exception e) {
            System.out.println("Error cargarComboClientes: " + e);
        }
    }

    private void cargarComboEquipos() {
        try {
            CMBEQUIPO.removeAllItems();
            ResultSet rs = eq.bd.ConsultaBD("SELECT IDEQUIPO, NOMBREEQUIPO FROM equipo");
            while (rs.next()) {
                int id = rs.getInt("IDEQUIPO");
                String nombre = rs.getString("NOMBREEQUIPO");
                CMBEQUIPO.addItem(id + " - " + nombre);
            }
            CMBEQUIPO.setSelectedIndex(-1);
        } catch (Exception e) {
            System.out.println("Error cargarComboEquipos: " + e);
        }
    }

    private void cargarComboTiempo() {
        DefaultComboBoxModel<String> m = new DefaultComboBoxModel<>();
        m.addElement("30M");
        m.addElement("45M");
        m.addElement("1H");
        m.addElement("1H30M");
        m.addElement("2H");
        CMBTIEMPO.setModel(m);
        CMBTIEMPO.setSelectedIndex(0);
    }

    private void seleccionarItemComboxU(JComboBox combo, String id) {
        for (int i = 0; i < combo.getItemCount(); i++) {
            String item = combo.getItemAt(i).toString();
            if (item.startsWith(id + " -")) {
                combo.setSelectedIndex(i);
                return;
            }
        }
    }

    private void cargarTablaUsoEquipo(String filtro) {
        DefaultTableModel modelo = new DefaultTableModel();
        modelo.addColumn("ID");
        modelo.addColumn("ID CLIENTE");
        modelo.addColumn("CLIENTE");
        modelo.addColumn("ID EQUIPO");
        modelo.addColumn("EQUIPO");
        modelo.addColumn("FECHA");
        modelo.addColumn("TIEMPO");

        try {
            ResultSet rs = uso.ConsultarUsoEquipo(filtro);

            while (rs.next()) {
                int minutos = rs.getInt("TIEMPO");
                String tiempoFormateado = formatearTiempoUsoEquipo(minutos);

                modelo.addRow(new Object[]{
                    rs.getInt("IDUSO"),
                    rs.getInt("IDCLIENTE"),
                    rs.getString("CLIENTE"),
                    rs.getInt("IDEQUIPO"),
                    rs.getString("EQUIPO"),
                    rs.getDate("FECHA"),
                    tiempoFormateado // Mostrar formato legible
                });
            }

            TABLAUSO.setModel(modelo);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error cargar tabla uso equipo: " + e);
            e.printStackTrace();
        }
    }

    private void limpiarU() {
        TXTIDUSO.setText("");
        CMBCLIENTE1.setSelectedIndex(-1);
        CMBEQUIPO.setSelectedIndex(-1);
        CMBTIEMPO.setSelectedIndex(-1);
        FECHAUSO.setDate(null);
    }

    private void limpiarTablaEntrenador() {
        DefaultTableModel model = (DefaultTableModel) TABLAENTRENADOR.getModel();
        model.setRowCount(0);
    }

    public void refrescarCombosClientesGlobal() {
        cargarComboCliente();
        cargarComboClientes();
    }

    private boolean existeEntrenador(String nombre) {
        try {
            ResultSet rs = entre.bd.ConsultaBD(
                    "SELECT * FROM entrenador WHERE NOMBRES = '" + nombre + "'"
            );
            return rs.next();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenu1 = new javax.swing.JMenu();
        jMenuBar2 = new javax.swing.JMenuBar();
        jMenu4 = new javax.swing.JMenu();
        jMenu5 = new javax.swing.JMenu();
        jMenuBar3 = new javax.swing.JMenuBar();
        jMenu6 = new javax.swing.JMenu();
        jMenu7 = new javax.swing.JMenu();
        jMenu8 = new javax.swing.JMenu();
        jSplitPane1 = new javax.swing.JSplitPane();
        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        txtIDCliente = new javax.swing.JTextField();
        TXTNOMBRESCLIENTES = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        TXTAPELLIDOSCLIENTE = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        TXTCELULARCLIENTES = new javax.swing.JTextField();
        TXTDIRECCIONCLIENTES = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        TXTCORREO = new javax.swing.JTextField();
        CALENDARIOCLIENTE = new com.toedter.calendar.JDateChooser();
        jLabel7 = new javax.swing.JLabel();
        btnAgregar = new javax.swing.JButton();
        btnConsultar = new javax.swing.JButton();
        btnEditar = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        TABLACLIENTE = new javax.swing.JTable();
        TXTBUSCARCLIENTE = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        Lfondo = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        TXTNOMBRETRADOR = new javax.swing.JTextField();
        TXTIDENTRENADOR = new javax.swing.JTextField();
        TXTCELULAR = new javax.swing.JTextField();
        TXTESPECIALIDAD = new javax.swing.JTextField();
        BTNAGREGARENTRENADOR = new javax.swing.JButton();
        BTNCONSULTARENTRENADOR = new javax.swing.JButton();
        BTNEDITARENTRENADOR = new javax.swing.JButton();
        BTNELIMINARENTRENADOR = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        TABLAENTRENADOR = new javax.swing.JTable();
        jLabel15 = new javax.swing.JLabel();
        TXTBUSCARENTRENADOR = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        TXTEQUIPO = new javax.swing.JTextField();
        TXTDESCRIPCION = new javax.swing.JTextField();
        CMBENTRENADOR = new javax.swing.JComboBox<>();
        TXTNAMEEQUIPO = new javax.swing.JTextField();
        TXTIPO = new javax.swing.JTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        TABLAEQUIPO = new javax.swing.JTable();
        BTNAGREGAR_EQUIIPO = new javax.swing.JButton();
        BTNCONSULTAREQUIPO = new javax.swing.JButton();
        BTNEDITAREQUIPO = new javax.swing.JButton();
        BTNELIMINAREQUIPO = new javax.swing.JButton();
        TXTBUSCAREQUIPO = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        CMBESTADO_EQUIPO1 = new javax.swing.JComboBox<>();
        jLabel25 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        BTNAGREGARPLAN = new javax.swing.JButton();
        BTNCONSULTARPLAN = new javax.swing.JButton();
        BTNEDITARPLAN = new javax.swing.JButton();
        BTNELIMINARPLAN = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        TABLAPLAN = new javax.swing.JTable();
        jScrollPane5 = new javax.swing.JScrollPane();
        TXDESCRIPCION = new javax.swing.JTextArea();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        TXTIDPLAN = new javax.swing.JTextField();
        TXTNOMBREPLAN = new javax.swing.JTextField();
        jLabel29 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        TXTBUSCARPLAN = new javax.swing.JTextField();
        jLabel31 = new javax.swing.JLabel();
        CMBCOSTO = new javax.swing.JComboBox<>();
        CMBDURACION = new javax.swing.JComboBox<>();
        jLabel32 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jLabel33 = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        TXTIDASISTENCIA = new javax.swing.JTextField();
        TXTCLIENTE = new javax.swing.JTextField();
        jScrollPane6 = new javax.swing.JScrollPane();
        TABLASISTENCIA = new javax.swing.JTable();
        jLabel35 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        BTNAGREGARASISTENCIA = new javax.swing.JButton();
        BTNCONSULTARASISTENCIAA = new javax.swing.JButton();
        BTNEDITARAASISTENCIA = new javax.swing.JButton();
        BTNELIMINARASISTENCIA = new javax.swing.JButton();
        FECHASISTENCIAA = new com.toedter.calendar.JDateChooser();
        jLabel37 = new javax.swing.JLabel();
        CMBHORASALIDA = new javax.swing.JComboBox<>();
        jLabel38 = new javax.swing.JLabel();
        CMBHORAENTRADA = new javax.swing.JComboBox<>();
        TXTBUSCARASISTENCIA = new javax.swing.JTextField();
        jLabel39 = new javax.swing.JLabel();
        jLabel40 = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jLabel41 = new javax.swing.JLabel();
        jLabel42 = new javax.swing.JLabel();
        jLabel43 = new javax.swing.JLabel();
        jLabel44 = new javax.swing.JLabel();
        BTNAGREGARCP = new javax.swing.JButton();
        jScrollPane7 = new javax.swing.JScrollPane();
        TABLA_CLIENTE_PLAN = new javax.swing.JTable();
        jLabel45 = new javax.swing.JLabel();
        BTNCONSULTARCP = new javax.swing.JButton();
        BTNEDITARCP = new javax.swing.JButton();
        TXTIDCLIENTE_PLAN = new javax.swing.JTextField();
        CMBCLIENTE = new javax.swing.JComboBox<>();
        CMBPLAN = new javax.swing.JComboBox<>();
        jLabel46 = new javax.swing.JLabel();
        CMBESTADO = new javax.swing.JComboBox<>();
        FECHAINICIO = new com.toedter.calendar.JDateChooser();
        FECHAFINAL = new com.toedter.calendar.JDateChooser();
        BTNELIMINARCP = new javax.swing.JButton();
        TXTBUSCARCP = new javax.swing.JTextField();
        jLabel47 = new javax.swing.JLabel();
        jLabel48 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel49 = new javax.swing.JLabel();
        jLabel50 = new javax.swing.JLabel();
        jLabel51 = new javax.swing.JLabel();
        jLabel52 = new javax.swing.JLabel();
        jScrollPane8 = new javax.swing.JScrollPane();
        TABLAUSO = new javax.swing.JTable();
        CMBCLIENTE1 = new javax.swing.JComboBox<>();
        CMBEQUIPO = new javax.swing.JComboBox<>();
        BTNCONSULTARUSO = new javax.swing.JButton();
        BTNEDITARUSO = new javax.swing.JButton();
        BTNELIMINARUSO = new javax.swing.JButton();
        jLabel53 = new javax.swing.JLabel();
        TXTIDUSO = new javax.swing.JTextField();
        BTNAGREGARUSO = new javax.swing.JButton();
        TXTBUSCARUSO = new javax.swing.JTextField();
        FECHAUSO = new com.toedter.calendar.JDateChooser();
        CMBTIEMPO = new javax.swing.JComboBox<>();
        jLabel54 = new javax.swing.JLabel();
        jLabel55 = new javax.swing.JLabel();

        jMenu1.setText("jMenu1");

        jMenu4.setText("File");
        jMenuBar2.add(jMenu4);

        jMenu5.setText("Edit");
        jMenuBar2.add(jMenu5);

        jMenu6.setText("File");
        jMenuBar3.add(jMenu6);

        jMenu7.setText("Edit");
        jMenuBar3.add(jMenu7);

        jMenu8.setText("jMenu8");

        jLabel1.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("ID CLIENTE");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jTabbedPane1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jTabbedPane1.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N

        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel10.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("ID CLIENTE");
        jPanel2.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 40, -1, -1));

        txtIDCliente.setEnabled(false);
        txtIDCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtIDClienteActionPerformed(evt);
            }
        });
        jPanel2.add(txtIDCliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 40, 50, -1));

        TXTNOMBRESCLIENTES.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TXTNOMBRESCLIENTESActionPerformed(evt);
            }
        });
        jPanel2.add(TXTNOMBRESCLIENTES, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 70, 150, -1));

        jLabel2.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("NOMBRES");
        jPanel2.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 70, -1, -1));

        jLabel3.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("APELLIDOS");
        jPanel2.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 100, -1, -1));
        jPanel2.add(TXTAPELLIDOSCLIENTE, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 100, 150, -1));

        jLabel4.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("CELULAR");
        jPanel2.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 130, -1, 20));
        jPanel2.add(TXTCELULARCLIENTES, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 130, 150, -1));
        jPanel2.add(TXTDIRECCIONCLIENTES, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 160, 150, -1));

        jLabel5.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("DIRECCION");
        jPanel2.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 160, -1, -1));

        jLabel6.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("CORREO");
        jPanel2.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 190, -1, -1));
        jPanel2.add(TXTCORREO, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 190, 150, -1));
        jPanel2.add(CALENDARIOCLIENTE, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 220, 100, 30));

        jLabel7.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("FECHA");
        jPanel2.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 220, 80, -1));

        btnAgregar.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        btnAgregar.setText("AGREGAR");
        btnAgregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarActionPerformed(evt);
            }
        });
        jPanel2.add(btnAgregar, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 410, -1, -1));

        btnConsultar.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        btnConsultar.setText("LIMPIAR");
        btnConsultar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnConsultarActionPerformed(evt);
            }
        });
        jPanel2.add(btnConsultar, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 410, -1, -1));

        btnEditar.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        btnEditar.setText("EDITAR");
        btnEditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditarActionPerformed(evt);
            }
        });
        jPanel2.add(btnEditar, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 410, -1, -1));

        btnEliminar.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        btnEliminar.setText("ELIMINAR");
        btnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarActionPerformed(evt);
            }
        });
        jPanel2.add(btnEliminar, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 410, -1, -1));

        TABLACLIENTE.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4", "Title 5", "Title 6", "Title 7"
            }
        ));
        TABLACLIENTE.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TABLACLIENTEMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(TABLACLIENTE);

        jPanel2.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 530, 710, 120));

        TXTBUSCARCLIENTE.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                TXTBUSCARCLIENTEKeyReleased(evt);
            }
        });
        jPanel2.add(TXTBUSCARCLIENTE, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 500, 710, 30));

        jLabel8.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel8.setForeground(java.awt.Color.white);
        jLabel8.setText("BUSCAR");
        jPanel2.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 470, -1, -1));

        Lfondo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Lfondo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gimnasio/fondo.jpg"))); // NOI18N
        jPanel2.add(Lfondo, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 730, 770));

        jTabbedPane1.addTab("CLIENTES", jPanel2);

        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel9.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("ENTRENADORES");
        jPanel4.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 80, 200, -1));

        jLabel11.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("NOMBRES:");
        jPanel4.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 180, -1, -1));

        jLabel12.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setText("ID:");
        jPanel4.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 150, -1, -1));

        jLabel13.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setText("CELULAR:");
        jPanel4.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 250, -1, -1));

        jLabel14.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(255, 255, 255));
        jLabel14.setText("ESPECIALIDAD:");
        jPanel4.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 210, 150, -1));
        jPanel4.add(TXTNOMBRETRADOR, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 180, 140, -1));

        TXTIDENTRENADOR.setEnabled(false);
        jPanel4.add(TXTIDENTRENADOR, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 150, 40, -1));
        jPanel4.add(TXTCELULAR, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 250, 140, -1));
        jPanel4.add(TXTESPECIALIDAD, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 210, 140, -1));

        BTNAGREGARENTRENADOR.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        BTNAGREGARENTRENADOR.setText("AGREGAR");
        BTNAGREGARENTRENADOR.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BTNAGREGARENTRENADORActionPerformed(evt);
            }
        });
        jPanel4.add(BTNAGREGARENTRENADOR, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 370, -1, -1));

        BTNCONSULTARENTRENADOR.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        BTNCONSULTARENTRENADOR.setText("LIMPIAR");
        BTNCONSULTARENTRENADOR.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BTNCONSULTARENTRENADORActionPerformed(evt);
            }
        });
        jPanel4.add(BTNCONSULTARENTRENADOR, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 370, -1, -1));

        BTNEDITARENTRENADOR.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        BTNEDITARENTRENADOR.setText("EDITAR");
        BTNEDITARENTRENADOR.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BTNEDITARENTRENADORActionPerformed(evt);
            }
        });
        jPanel4.add(BTNEDITARENTRENADOR, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 370, -1, -1));

        BTNELIMINARENTRENADOR.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        BTNELIMINARENTRENADOR.setText("ELIMINAR");
        BTNELIMINARENTRENADOR.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BTNELIMINARENTRENADORActionPerformed(evt);
            }
        });
        jPanel4.add(BTNELIMINARENTRENADOR, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 370, -1, -1));

        TABLAENTRENADOR.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        TABLAENTRENADOR.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TABLAENTRENADORMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(TABLAENTRENADOR);

        jPanel4.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 560, 620, 110));

        jLabel15.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel15.setForeground(java.awt.Color.white);
        jLabel15.setText("BUSCAR");
        jPanel4.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 490, -1, -1));

        TXTBUSCARENTRENADOR.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TXTBUSCARENTRENADORActionPerformed(evt);
            }
        });
        TXTBUSCARENTRENADOR.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                TXTBUSCARENTRENADORKeyReleased(evt);
            }
        });
        jPanel4.add(TXTBUSCARENTRENADOR, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 530, 620, 30));

        jLabel17.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gimnasio/Gemini_Generated_Image_qb9mmeqb9mmeqb9m.png"))); // NOI18N
        jLabel17.setText("jLabel17");
        jLabel17.setMinimumSize(new java.awt.Dimension(735, 760));
        jPanel4.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 735, 760));

        jTabbedPane1.addTab("ENTRENADORES", jPanel4);

        jPanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel16.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        jLabel16.setText("EQUIPOS");
        jPanel5.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 80, -1, -1));

        TXTEQUIPO.setEnabled(false);
        jPanel5.add(TXTEQUIPO, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 200, 50, -1));
        jPanel5.add(TXTDESCRIPCION, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 300, 100, -1));

        CMBENTRENADOR.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jPanel5.add(CMBENTRENADOR, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 250, -1, -1));
        jPanel5.add(TXTNAMEEQUIPO, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 300, 130, -1));
        jPanel5.add(TXTIPO, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 200, 90, -1));

        TABLAEQUIPO.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        TABLAEQUIPO.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TABLAEQUIPOMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(TABLAEQUIPO);

        jPanel5.add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 590, 570, 100));

        BTNAGREGAR_EQUIIPO.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        BTNAGREGAR_EQUIIPO.setText("AGREGAR");
        BTNAGREGAR_EQUIIPO.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BTNAGREGAR_EQUIIPOActionPerformed(evt);
            }
        });
        jPanel5.add(BTNAGREGAR_EQUIIPO, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 410, -1, -1));

        BTNCONSULTAREQUIPO.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        BTNCONSULTAREQUIPO.setText("LIMPIAR");
        BTNCONSULTAREQUIPO.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BTNCONSULTAREQUIPOActionPerformed(evt);
            }
        });
        jPanel5.add(BTNCONSULTAREQUIPO, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 410, -1, -1));

        BTNEDITAREQUIPO.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        BTNEDITAREQUIPO.setText("EDITAR");
        BTNEDITAREQUIPO.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BTNEDITAREQUIPOActionPerformed(evt);
            }
        });
        jPanel5.add(BTNEDITAREQUIPO, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 410, -1, -1));

        BTNELIMINAREQUIPO.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        BTNELIMINAREQUIPO.setText("ELIMINAR");
        BTNELIMINAREQUIPO.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BTNELIMINAREQUIPOActionPerformed(evt);
            }
        });
        jPanel5.add(BTNELIMINAREQUIPO, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 410, -1, -1));

        TXTBUSCAREQUIPO.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                TXTBUSCAREQUIPOKeyReleased(evt);
            }
        });
        jPanel5.add(TXTBUSCAREQUIPO, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 560, 570, 30));

        jLabel18.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel18.setText("BUSCAR:");
        jPanel5.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 530, -1, -1));

        jLabel19.setFont(new java.awt.Font("Arial Black", 1, 14)); // NOI18N
        jLabel19.setForeground(java.awt.Color.white);
        jLabel19.setText("ID:");
        jPanel5.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 200, 30, -1));

        jLabel20.setBackground(new java.awt.Color(0, 0, 0));
        jLabel20.setFont(new java.awt.Font("Arial Black", 1, 14)); // NOI18N
        jLabel20.setText("ENTRENADOR:");
        jPanel5.add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 250, -1, -1));

        jLabel21.setBackground(new java.awt.Color(0, 0, 0));
        jLabel21.setFont(new java.awt.Font("Arial Black", 1, 14)); // NOI18N
        jLabel21.setText("NOMBRE:");
        jPanel5.add(jLabel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 300, 90, -1));

        jLabel22.setBackground(new java.awt.Color(0, 0, 0));
        jLabel22.setFont(new java.awt.Font("Arial Black", 1, 14)); // NOI18N
        jLabel22.setText("TIPO:");
        jPanel5.add(jLabel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 200, -1, -1));

        jLabel23.setBackground(new java.awt.Color(0, 0, 0));
        jLabel23.setFont(new java.awt.Font("Arial Black", 1, 14)); // NOI18N
        jLabel23.setText("ESTADO:");
        jPanel5.add(jLabel23, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 250, -1, -1));

        jLabel24.setBackground(new java.awt.Color(0, 0, 0));
        jLabel24.setFont(new java.awt.Font("Arial Black", 1, 14)); // NOI18N
        jLabel24.setText("DESCRPCION:");
        jPanel5.add(jLabel24, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 300, -1, -1));

        CMBESTADO_EQUIPO1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jPanel5.add(CMBESTADO_EQUIPO1, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 250, -1, -1));

        jLabel25.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gimnasio/Gemini_Generated_Image_bqmmv7bqmmv7bqmm.png"))); // NOI18N
        jLabel25.setMinimumSize(new java.awt.Dimension(735, 760));
        jPanel5.add(jLabel25, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 730, 760));

        jTabbedPane1.addTab("EQUIPOS", jPanel5);

        jPanel6.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        BTNAGREGARPLAN.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        BTNAGREGARPLAN.setText("AGREGAR");
        BTNAGREGARPLAN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BTNAGREGARPLANActionPerformed(evt);
            }
        });
        jPanel6.add(BTNAGREGARPLAN, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 390, -1, -1));

        BTNCONSULTARPLAN.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        BTNCONSULTARPLAN.setText("LIMPIAR");
        BTNCONSULTARPLAN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BTNCONSULTARPLANActionPerformed(evt);
            }
        });
        jPanel6.add(BTNCONSULTARPLAN, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 390, -1, -1));

        BTNEDITARPLAN.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        BTNEDITARPLAN.setText("EDITAR");
        BTNEDITARPLAN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BTNEDITARPLANActionPerformed(evt);
            }
        });
        jPanel6.add(BTNEDITARPLAN, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 390, -1, -1));

        BTNELIMINARPLAN.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        BTNELIMINARPLAN.setText("ELIMINAR");
        BTNELIMINARPLAN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BTNELIMINARPLANActionPerformed(evt);
            }
        });
        jPanel6.add(BTNELIMINARPLAN, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 390, -1, -1));

        TABLAPLAN.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        TABLAPLAN.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TABLAPLANMouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(TABLAPLAN);

        jPanel6.add(jScrollPane4, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 550, 510, 188));

        TXDESCRIPCION.setColumns(20);
        TXDESCRIPCION.setRows(5);
        jScrollPane5.setViewportView(TXDESCRIPCION);

        jPanel6.add(jScrollPane5, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 270, 448, 58));

        jLabel26.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel26.setText("ID:");
        jPanel6.add(jLabel26, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 40, -1, -1));

        jLabel27.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel27.setText("NOMBRE DEL PLAN:");
        jPanel6.add(jLabel27, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 80, -1, -1));

        jLabel28.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel28.setText("COSTO:");
        jPanel6.add(jLabel28, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 120, -1, -1));

        TXTIDPLAN.setEnabled(false);
        TXTIDPLAN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TXTIDPLANActionPerformed(evt);
            }
        });
        jPanel6.add(TXTIDPLAN, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 40, 40, -1));

        TXTNOMBREPLAN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TXTNOMBREPLANActionPerformed(evt);
            }
        });
        jPanel6.add(TXTNOMBREPLAN, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 80, 215, -1));

        jLabel29.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel29.setText("DURACION:");
        jPanel6.add(jLabel29, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 160, -1, -1));

        jLabel30.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel30.setText("DESCRIPCION:");
        jPanel6.add(jLabel30, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 220, -1, -1));

        TXTBUSCARPLAN.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                TXTBUSCARPLANKeyReleased(evt);
            }
        });
        jPanel6.add(TXTBUSCARPLAN, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 520, 510, 30));

        jLabel31.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel31.setForeground(new java.awt.Color(255, 255, 255));
        jLabel31.setText("BUSCAR:");
        jPanel6.add(jLabel31, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 480, -1, -1));

        CMBCOSTO.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jPanel6.add(CMBCOSTO, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 120, -1, -1));

        CMBDURACION.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jPanel6.add(CMBDURACION, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 160, -1, -1));

        jLabel32.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gimnasio/Gemini_Generated_Image_b2qyl2b2qyl2b2qy.png"))); // NOI18N
        jLabel32.setText("jLabel32");
        jLabel32.setMinimumSize(new java.awt.Dimension(735, 760));
        jPanel6.add(jLabel32, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 740, 760));

        jTabbedPane1.addTab("PLANES", jPanel6);

        jPanel7.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel33.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel33.setForeground(new java.awt.Color(255, 255, 255));
        jLabel33.setText("ASISTENCIAS DE LOS CLIENTES");
        jPanel7.add(jLabel33, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 40, 290, 30));

        jLabel34.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel34.setForeground(new java.awt.Color(255, 255, 255));
        jLabel34.setText("ID:");
        jPanel7.add(jLabel34, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 120, 30, 20));

        TXTIDASISTENCIA.setEnabled(false);
        TXTIDASISTENCIA.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TXTIDASISTENCIAActionPerformed(evt);
            }
        });
        jPanel7.add(TXTIDASISTENCIA, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 120, 40, -1));
        jPanel7.add(TXTCLIENTE, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 190, 140, -1));

        TABLASISTENCIA.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        TABLASISTENCIA.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TABLASISTENCIAMouseClicked(evt);
            }
        });
        jScrollPane6.setViewportView(TABLASISTENCIA);

        jPanel7.add(jScrollPane6, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 580, 590, 110));

        jLabel35.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel35.setForeground(new java.awt.Color(255, 255, 255));
        jLabel35.setText("CLIENTE:");
        jPanel7.add(jLabel35, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 190, 90, 20));

        jLabel36.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel36.setForeground(new java.awt.Color(255, 255, 255));
        jLabel36.setText("HORA DE SALIDA:");
        jPanel7.add(jLabel36, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 240, 170, 20));

        BTNAGREGARASISTENCIA.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        BTNAGREGARASISTENCIA.setText("AGREGAR");
        BTNAGREGARASISTENCIA.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BTNAGREGARASISTENCIAActionPerformed(evt);
            }
        });
        jPanel7.add(BTNAGREGARASISTENCIA, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 400, -1, -1));

        BTNCONSULTARASISTENCIAA.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        BTNCONSULTARASISTENCIAA.setText("LIMPIAR");
        BTNCONSULTARASISTENCIAA.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BTNCONSULTARASISTENCIAAActionPerformed(evt);
            }
        });
        jPanel7.add(BTNCONSULTARASISTENCIAA, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 400, -1, -1));

        BTNEDITARAASISTENCIA.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        BTNEDITARAASISTENCIA.setText("EDITAR");
        BTNEDITARAASISTENCIA.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BTNEDITARAASISTENCIAActionPerformed(evt);
            }
        });
        jPanel7.add(BTNEDITARAASISTENCIA, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 400, -1, -1));

        BTNELIMINARASISTENCIA.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        BTNELIMINARASISTENCIA.setText("ELIMINAR");
        BTNELIMINARASISTENCIA.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BTNELIMINARASISTENCIAActionPerformed(evt);
            }
        });
        jPanel7.add(BTNELIMINARASISTENCIA, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 400, -1, -1));
        jPanel7.add(FECHASISTENCIAA, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 260, 120, -1));

        jLabel37.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel37.setForeground(java.awt.Color.white);
        jLabel37.setText("FECHA:");
        jPanel7.add(jLabel37, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 260, -1, -1));

        CMBHORASALIDA.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jPanel7.add(CMBHORASALIDA, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 240, -1, -1));

        jLabel38.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel38.setForeground(new java.awt.Color(255, 255, 255));
        jLabel38.setText("HORA DE ENTRADA:");
        jPanel7.add(jLabel38, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 160, 180, 20));

        CMBHORAENTRADA.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jPanel7.add(CMBHORAENTRADA, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 160, -1, -1));

        TXTBUSCARASISTENCIA.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                TXTBUSCARASISTENCIAKeyReleased(evt);
            }
        });
        jPanel7.add(TXTBUSCARASISTENCIA, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 550, 590, 30));

        jLabel39.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel39.setForeground(java.awt.Color.white);
        jLabel39.setText("BUSCAR:");
        jPanel7.add(jLabel39, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 510, -1, -1));

        jLabel40.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gimnasio/45641_1.png"))); // NOI18N
        jLabel40.setText("jLabel40");
        jPanel7.add(jLabel40, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 740, 760));

        jTabbedPane1.addTab("ASISTENCIA", jPanel7);

        jPanel8.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel41.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel41.setForeground(java.awt.Color.white);
        jLabel41.setText("ID:");
        jPanel8.add(jLabel41, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 70, 30, 20));

        jLabel42.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel42.setForeground(java.awt.Color.white);
        jLabel42.setText("CLIENTE:");
        jPanel8.add(jLabel42, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 110, -1, -1));

        jLabel43.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel43.setForeground(java.awt.Color.white);
        jLabel43.setText("PLAN:");
        jPanel8.add(jLabel43, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 150, -1, -1));

        jLabel44.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel44.setForeground(java.awt.Color.white);
        jLabel44.setText("FECHA DE INICIO:");
        jPanel8.add(jLabel44, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 190, -1, -1));

        BTNAGREGARCP.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        BTNAGREGARCP.setText("AGREGAR");
        BTNAGREGARCP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BTNAGREGARCPActionPerformed(evt);
            }
        });
        jPanel8.add(BTNAGREGARCP, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 390, 120, 40));

        TABLA_CLIENTE_PLAN.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        TABLA_CLIENTE_PLAN.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TABLA_CLIENTE_PLANMouseClicked(evt);
            }
        });
        jScrollPane7.setViewportView(TABLA_CLIENTE_PLAN);

        jPanel8.add(jScrollPane7, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 590, 600, 134));

        jLabel45.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel45.setForeground(java.awt.Color.white);
        jLabel45.setText("FECHA FINAL:");
        jPanel8.add(jLabel45, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 230, -1, -1));

        BTNCONSULTARCP.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        BTNCONSULTARCP.setText("LIMPIAR");
        BTNCONSULTARCP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BTNCONSULTARCPActionPerformed(evt);
            }
        });
        jPanel8.add(BTNCONSULTARCP, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 390, 120, 40));

        BTNEDITARCP.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        BTNEDITARCP.setText("EDITAR");
        BTNEDITARCP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BTNEDITARCPActionPerformed(evt);
            }
        });
        jPanel8.add(BTNEDITARCP, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 390, 100, 40));

        TXTIDCLIENTE_PLAN.setEnabled(false);
        jPanel8.add(TXTIDCLIENTE_PLAN, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 70, 50, -1));

        jPanel8.add(CMBCLIENTE, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 110, -1, -1));

        jPanel8.add(CMBPLAN, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 150, 100, -1));

        jLabel46.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel46.setForeground(java.awt.Color.white);
        jLabel46.setText("ESTADO:");
        jPanel8.add(jLabel46, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 270, 70, -1));

        CMBESTADO.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jPanel8.add(CMBESTADO, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 270, -1, -1));
        jPanel8.add(FECHAINICIO, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 190, 130, -1));
        jPanel8.add(FECHAFINAL, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 230, 150, -1));

        BTNELIMINARCP.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        BTNELIMINARCP.setText("ELIMINAR");
        BTNELIMINARCP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BTNELIMINARCPActionPerformed(evt);
            }
        });
        jPanel8.add(BTNELIMINARCP, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 390, 120, 40));

        TXTBUSCARCP.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                TXTBUSCARCPKeyReleased(evt);
            }
        });
        jPanel8.add(TXTBUSCARCP, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 560, 600, 30));

        jLabel47.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel47.setForeground(java.awt.Color.white);
        jLabel47.setText("BUSCAR:");
        jPanel8.add(jLabel47, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 530, -1, -1));

        jLabel48.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gimnasio/Gemini_Generated_Image_b2qyl2b2qyl2b2qy.png"))); // NOI18N
        jLabel48.setText("jLabel48");
        jPanel8.add(jLabel48, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 740, 760));

        jTabbedPane1.addTab("PLANES DE CLIENTES", jPanel8);

        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel49.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel49.setText("Cliente:");
        jPanel3.add(jLabel49, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 80, -1, -1));

        jLabel50.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel50.setText("Equipo:");
        jPanel3.add(jLabel50, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 140, -1, -1));

        jLabel51.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel51.setText("Fecha:");
        jPanel3.add(jLabel51, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 200, -1, -1));

        jLabel52.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel52.setText("Tiempo:");
        jPanel3.add(jLabel52, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 260, -1, -1));

        TABLAUSO.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        TABLAUSO.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TABLAUSOMouseClicked(evt);
            }
        });
        jScrollPane8.setViewportView(TABLAUSO);

        jPanel3.add(jScrollPane8, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 580, 600, 122));

        CMBCLIENTE1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jPanel3.add(CMBCLIENTE1, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 80, -1, -1));

        CMBEQUIPO.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jPanel3.add(CMBEQUIPO, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 140, -1, -1));

        BTNCONSULTARUSO.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        BTNCONSULTARUSO.setText("LIMPIAR");
        BTNCONSULTARUSO.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BTNCONSULTARUSOActionPerformed(evt);
            }
        });
        jPanel3.add(BTNCONSULTARUSO, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 390, -1, -1));

        BTNEDITARUSO.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        BTNEDITARUSO.setText("EDITAR");
        BTNEDITARUSO.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BTNEDITARUSOActionPerformed(evt);
            }
        });
        jPanel3.add(BTNEDITARUSO, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 390, -1, -1));

        BTNELIMINARUSO.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        BTNELIMINARUSO.setText("ELIMINAR");
        BTNELIMINARUSO.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BTNELIMINARUSOActionPerformed(evt);
            }
        });
        jPanel3.add(BTNELIMINARUSO, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 390, -1, -1));

        jLabel53.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel53.setForeground(new java.awt.Color(255, 255, 255));
        jLabel53.setText("ID:");
        jPanel3.add(jLabel53, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 30, -1, -1));

        TXTIDUSO.setEnabled(false);
        jPanel3.add(TXTIDUSO, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 30, 40, -1));

        BTNAGREGARUSO.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        BTNAGREGARUSO.setText("AGREGAR");
        BTNAGREGARUSO.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BTNAGREGARUSOActionPerformed(evt);
            }
        });
        jPanel3.add(BTNAGREGARUSO, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 390, -1, -1));

        TXTBUSCARUSO.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                TXTBUSCARUSOKeyReleased(evt);
            }
        });
        jPanel3.add(TXTBUSCARUSO, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 550, 600, 30));
        jPanel3.add(FECHAUSO, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 200, -1, -1));

        CMBTIEMPO.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jPanel3.add(CMBTIEMPO, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 260, -1, -1));

        jLabel54.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel54.setText("BUSCAR:");
        jPanel3.add(jLabel54, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 500, -1, -1));

        jLabel55.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gimnasio/Gemini_Generated_Image_h03n24h03n24h03n ..png"))); // NOI18N
        jLabel55.setText("jLabel55");
        jLabel55.setMinimumSize(new java.awt.Dimension(735, 760));
        jPanel3.add(jLabel55, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 740, 770));

        jTabbedPane1.addTab("USO DE EQUIPOS", jPanel3);

        jPanel1.add(jTabbedPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 740, 790));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 791, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtIDClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtIDClienteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtIDClienteActionPerformed

    private void TXTNOMBRESCLIENTESActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TXTNOMBRESCLIENTESActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TXTNOMBRESCLIENTESActionPerformed

    private void btnAgregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarActionPerformed

        try {
            cli.setNombres(TXTNOMBRESCLIENTES.getText());
            cli.setApellidos(TXTAPELLIDOSCLIENTE.getText());
            cli.setCelular(TXTCELULARCLIENTES.getText());
            cli.setDireccion(TXTDIRECCIONCLIENTES.getText());
            cli.setCorreo(TXTCORREO.getText());

            // fecha a String
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            cli.setFecha_Registro(sdf.format(CALENDARIOCLIENTE.getDate()));

            cli.InsertarCliente();
            JOptionPane.showMessageDialog(this, "Cliente insertado");

            cargarTabla("");
            cargarComboCliente();      // planes
            cargarComboClientes();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al insertar: " + e);
        }
        limpiarC();
        refrescarCombosClientesGlobal();
        cargarComboCliente();    // Plan cliente
        cargarComboClientes();   // Uso equipo
        cargarComboPlan();

    }//GEN-LAST:event_btnAgregarActionPerformed

    private void btnConsultarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConsultarActionPerformed
        cargarTabla("");
        limpiarC();
    }//GEN-LAST:event_btnConsultarActionPerformed

    private void btnEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditarActionPerformed
        if (txtIDCliente.getText().equals("")) {
            JOptionPane.showMessageDialog(this, "Seleccione un cliente");
            return;
        }

        try {
            cli.setId_Cliente(Integer.parseInt(txtIDCliente.getText()));
            cli.setNombres(TXTNOMBRESCLIENTES.getText());
            cli.setApellidos(TXTAPELLIDOSCLIENTE.getText());
            cli.setCelular(TXTCELULARCLIENTES.getText());
            cli.setDireccion(TXTDIRECCIONCLIENTES.getText());
            cli.setCorreo(TXTCORREO.getText());

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            cli.setFecha_Registro(sdf.format(CALENDARIOCLIENTE.getDate()));

            cli.ActualizarCliente();
            JOptionPane.showMessageDialog(this, "Cliente actualizado");

            cargarTabla("");
            limpiarC();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al actualizar: " + e);
        }
    }//GEN-LAST:event_btnEditarActionPerformed

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
        if (txtIDCliente.getText().equals("")) {
            JOptionPane.showMessageDialog(this, "Seleccione un cliente");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "¿Eliminar cliente?", "Confirmar", JOptionPane.YES_NO_OPTION);

        if (confirm == 0) {
            try {
                cli.setId_Cliente(Integer.parseInt(txtIDCliente.getText()));
                cli.EliminarCliente();
                JOptionPane.showMessageDialog(this, "Cliente eliminado");

                cargarTabla("");
            } catch (Exception ex) {

            }
        }
        limpiarC();
    }//GEN-LAST:event_btnEliminarActionPerformed

    private void TABLACLIENTEMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TABLACLIENTEMouseClicked
        int fila = TABLACLIENTE.getSelectedRow();

        txtIDCliente.setText(TABLACLIENTE.getValueAt(fila, 0).toString());
        TXTNOMBRESCLIENTES.setText(TABLACLIENTE.getValueAt(fila, 1).toString());
        TXTAPELLIDOSCLIENTE.setText(TABLACLIENTE.getValueAt(fila, 2).toString());
        TXTCELULARCLIENTES.setText(TABLACLIENTE.getValueAt(fila, 3).toString());
        TXTDIRECCIONCLIENTES.setText(TABLACLIENTE.getValueAt(fila, 4).toString());
        TXTCORREO.setText(TABLACLIENTE.getValueAt(fila, 5).toString());

        try {
            java.util.Date fecha = java.sql.Date.valueOf(TABLACLIENTE.getValueAt(fila, 6).toString());
            CALENDARIOCLIENTE.setDate(fecha);
        } catch (Exception e) {
        }
    }//GEN-LAST:event_TABLACLIENTEMouseClicked

    private void TXTBUSCARCLIENTEKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TXTBUSCARCLIENTEKeyReleased

        cargarTabla(TXTBUSCARCLIENTE.getText());

    }//GEN-LAST:event_TXTBUSCARCLIENTEKeyReleased

    private void BTNAGREGARENTRENADORActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BTNAGREGARENTRENADORActionPerformed
        try {
            if (existeEntrenador(TXTNOMBRETRADOR.getText())) {
                JOptionPane.showMessageDialog(this, "Este entrenador ya existe.");
                return;
            }

            entre.setNombres(TXTNOMBRETRADOR.getText());
            entre.setEspecialidad(TXTESPECIALIDAD.getText());
            entre.setCelular(TXTCELULAR.getText());
            entre.InsertarEntrenador();

            JOptionPane.showMessageDialog(this, "Entrenador registrado!");

            limpiarENTRE();
            cargarTablaEntrenador("");
            cargarComboEntrenador();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e);
        }
    }//GEN-LAST:event_BTNAGREGARENTRENADORActionPerformed

    private void BTNCONSULTARENTRENADORActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BTNCONSULTARENTRENADORActionPerformed
        cargarTablaEntrenador("");
        limpiarENTRE();
    }//GEN-LAST:event_BTNCONSULTARENTRENADORActionPerformed

    private void BTNEDITARENTRENADORActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BTNEDITARENTRENADORActionPerformed
        try {
            entre.setId_Entrenador(Integer.parseInt(TXTIDENTRENADOR.getText()));
            entre.setNombres(TXTNOMBRETRADOR.getText());
            entre.setEspecialidad(TXTESPECIALIDAD.getText());
            entre.setCelular(TXTCELULAR.getText());

            entre.ActualizarEntrenador();

            cargarTablaEntrenador("");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e);
        }
    }//GEN-LAST:event_BTNEDITARENTRENADORActionPerformed

    private void BTNELIMINARENTRENADORActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BTNELIMINARENTRENADORActionPerformed
        try {
            entre.setId_Entrenador(Integer.parseInt(TXTIDENTRENADOR.getText()));
            entre.EliminarEntrenador();
            cargarTablaEntrenador("");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e);
        }
        if (TXTIDENTRENADOR.getText().equals("")) {
            JOptionPane.showMessageDialog(this, "Seleccione un entrenador");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "¿Eliminar cliente?", "Confirmar", JOptionPane.YES_NO_OPTION);

        if (confirm == 0) {
            try {
                entre.setId_Entrenador(Integer.parseInt(TXTIDENTRENADOR.getText()));
                entre.EliminarEntrenador();
                JOptionPane.showMessageDialog(this, "Entrenador eliminado");

                cargarTablaEntrenador("");
            } catch (Exception ex) {

            }
        }
    }//GEN-LAST:event_BTNELIMINARENTRENADORActionPerformed

    private void TABLAENTRENADORMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TABLAENTRENADORMouseClicked
        int fila = TABLAENTRENADOR.getSelectedRow();
        if (fila >= 0) {
            TXTIDENTRENADOR.setText(TABLAENTRENADOR.getValueAt(fila, 0).toString());
            TXTNOMBRETRADOR.setText(TABLAENTRENADOR.getValueAt(fila, 1).toString());
            TXTESPECIALIDAD.setText(TABLAENTRENADOR.getValueAt(fila, 2).toString());
            TXTCELULAR.setText(TABLAENTRENADOR.getValueAt(fila, 3).toString());
        }
    }//GEN-LAST:event_TABLAENTRENADORMouseClicked

    private void TXTBUSCARENTRENADORActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TXTBUSCARENTRENADORActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TXTBUSCARENTRENADORActionPerformed

    private void TXTBUSCARENTRENADORKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TXTBUSCARENTRENADORKeyReleased
        cargarTablaEntrenador(TXTBUSCARENTRENADOR.getText());
    }//GEN-LAST:event_TXTBUSCARENTRENADORKeyReleased

    private void TABLAEQUIPOMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TABLAEQUIPOMouseClicked
        int fila = TABLAEQUIPO.getSelectedRow();

        TXTEQUIPO.setText(TABLAEQUIPO.getValueAt(fila, 0).toString()); // ID

        // CAMBIO: Pasar solo el ID del entrenador
        String idEntrenador = TABLAEQUIPO.getValueAt(fila, 1).toString();
        seleccionarItemCombo(CMBENTRENADOR, idEntrenador);

        TXTNAMEEQUIPO.setText(TABLAEQUIPO.getValueAt(fila, 3).toString());
        TXTIPO.setText(TABLAEQUIPO.getValueAt(fila, 4).toString());
        CMBESTADO_EQUIPO1.setSelectedItem(TABLAEQUIPO.getValueAt(fila, 5).toString());
        TXTDESCRIPCION.setText(TABLAEQUIPO.getValueAt(fila, 6).toString());
    }//GEN-LAST:event_TABLAEQUIPOMouseClicked

    private void BTNAGREGAR_EQUIIPOActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BTNAGREGAR_EQUIIPOActionPerformed
        try {
            String item = CMBENTRENADOR.getSelectedItem().toString();
            int idEnt = Integer.parseInt(item.split(" - ")[0]);

            eq.setId_Entrenador(idEnt);
            eq.setNombre_Equipo(TXTNAMEEQUIPO.getText());
            eq.setTipo(TXTIPO.getText());
            eq.setEstado(CMBESTADO_EQUIPO1.getSelectedItem().toString()); // ✅
            eq.setDescripcion(TXTDESCRIPCION.getText());

            eq.InsertarEquipo();
            cargarTablaEquipo("");
            limpiarE();

        } catch (Exception e) {
            System.out.println("Error insertar equipo: " + e);
        }
    }//GEN-LAST:event_BTNAGREGAR_EQUIIPOActionPerformed

    private void BTNCONSULTAREQUIPOActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BTNCONSULTAREQUIPOActionPerformed
        cargarTablaEquipo("");
        limpiarE();
    }//GEN-LAST:event_BTNCONSULTAREQUIPOActionPerformed

    private void BTNEDITAREQUIPOActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BTNEDITAREQUIPOActionPerformed
        try {
            if (TXTEQUIPO.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Seleccione un equipo");
                return;
            }

            eq.setId_Equipo(Integer.parseInt(TXTEQUIPO.getText()));

            String item = CMBENTRENADOR.getSelectedItem().toString();
            int idEnt = Integer.parseInt(item.split(" - ")[0]);

            eq.setId_Entrenador(idEnt);
            eq.setNombre_Equipo(TXTNAMEEQUIPO.getText());
            eq.setTipo(TXTIPO.getText());
            eq.setEstado(CMBESTADO_EQUIPO1.getSelectedItem().toString());
            eq.setDescripcion(TXTDESCRIPCION.getText());

            eq.ActualizarEquipo();

            JOptionPane.showMessageDialog(this, "Equipo actualizado correctamente");
            cargarTablaEquipo("");
            limpiarE();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al actualizar equipo: " + e.getMessage());
            e.printStackTrace();
        }
    }//GEN-LAST:event_BTNEDITAREQUIPOActionPerformed

    private void BTNELIMINAREQUIPOActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BTNELIMINAREQUIPOActionPerformed
        try {
            eq.setId_Equipo(Integer.parseInt(TXTEQUIPO.getText()));
            eq.EliminarEquipo();
            cargarTablaEquipo("");

        } catch (Exception e) {
            System.out.println("Error eliminar equipo: " + e);
        }
    }

    // ---------------------------------------------------------------
    //  BUSCAR EN TIEMPO REAL
    // ---------------------------------------------------------------
    private void TXTBUSCARKeyReleased(java.awt.event.KeyEvent evt) {
        cargarTablaEquipo(TXTBUSCAREQUIPO.getText());
    }//GEN-LAST:event_BTNELIMINAREQUIPOActionPerformed

    private void TXTBUSCAREQUIPOKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TXTBUSCAREQUIPOKeyReleased
        cargarTablaEquipo(TXTBUSCAREQUIPO.getText());
    }//GEN-LAST:event_TXTBUSCAREQUIPOKeyReleased

    private void BTNAGREGARPLANActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BTNAGREGARPLANActionPerformed
        try {
            plan.setNombre_plan(TXTNOMBREPLAN.getText());
            plan.setCosto(obtenerCosto());
            plan.setDuracion(obtenerDuracion());
            plan.setDescripcion(TXDESCRIPCION.getText());

            plan.InsertarPlan();
            JOptionPane.showMessageDialog(null, "PLAN REGISTRADO");

            cargarTablaPlan("");
            cargarComboPlan();
            limpiarP();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error al guardar: " + ex);
        }


    }//GEN-LAST:event_BTNAGREGARPLANActionPerformed

    private void BTNEDITARPLANActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BTNEDITARPLANActionPerformed
        try {
            plan.setId_Plan(Integer.parseInt(TXTIDPLAN.getText()));
            plan.setNombre_plan(TXTNOMBREPLAN.getText());
            plan.setCosto(obtenerCosto());
            plan.setDuracion(obtenerDuracion());
            plan.setDescripcion(TXDESCRIPCION.getText());

            plan.ActualizarPlan();
            JOptionPane.showMessageDialog(null, "PLAN ACTUALIZADO");

            cargarTablaPlan("");
            limpiarP();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error al modificar: " + ex);
        }
        limpiarP();
    }//GEN-LAST:event_BTNEDITARPLANActionPerformed

    private void BTNELIMINARPLANActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BTNELIMINARPLANActionPerformed
        try {
            plan.setId_Plan(Integer.parseInt(TXTIDPLAN.getText()));
            plan.EliminarPlan();
            JOptionPane.showMessageDialog(null, "PLAN ELIMINADO");

            cargarTablaPlan("");
            limpiarP();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error al eliminar: " + ex);
        }
        limpiarP();
    }//GEN-LAST:event_BTNELIMINARPLANActionPerformed

    private void TABLAPLANMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TABLAPLANMouseClicked
        int fila = TABLAPLAN.getSelectedRow();
        if (fila != -1) {
            TXTIDPLAN.setText(TABLAPLAN.getValueAt(fila, 0).toString());
            TXTNOMBREPLAN.setText(TABLAPLAN.getValueAt(fila, 1).toString());

            // COSTO - Convertir a formato con $
            double costoNum = Double.parseDouble(TABLAPLAN.getValueAt(fila, 2).toString());
            String costoStr = (int) costoNum + "$";
            CMBCOSTO.setSelectedItem(costoStr);

            // DURACION COMBO
            int dur = Integer.parseInt(TABLAPLAN.getValueAt(fila, 3).toString());
            seleccionarDuracionCombo(dur);

            TXDESCRIPCION.setText(TABLAPLAN.getValueAt(fila, 4).toString());
        }
    }//GEN-LAST:event_TABLAPLANMouseClicked

    private void TXTIDPLANActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TXTIDPLANActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TXTIDPLANActionPerformed

    private void TXTNOMBREPLANActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TXTNOMBREPLANActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TXTNOMBREPLANActionPerformed

    private void TXTBUSCARPLANKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TXTBUSCARPLANKeyReleased
        cargarTablaPlan(TXTBUSCARPLAN.getText());
    }//GEN-LAST:event_TXTBUSCARPLANKeyReleased

    private void TXTIDASISTENCIAActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TXTIDASISTENCIAActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TXTIDASISTENCIAActionPerformed

    private void TABLASISTENCIAMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TABLASISTENCIAMouseClicked
        int fila = TABLASISTENCIA.getSelectedRow();
        if (fila != -1) {
            // ID de asistencia (puede estar vacío si es un cliente sin asistencia)
            Object idAsist = TABLASISTENCIA.getValueAt(fila, 0);
            TXTIDASISTENCIA.setText(idAsist != null && !idAsist.toString().isEmpty() ? idAsist.toString() : "");

            // Nombre del cliente (SIEMPRE debe tener valor)
            TXTCLIENTE.setText(TABLASISTENCIA.getValueAt(fila, 2).toString());

            try {
                // Cargar fecha (SIEMPRE debe tener valor: fecha de asistencia o fecha de registro)
                Object fechaObj = TABLASISTENCIA.getValueAt(fila, 3);
                if (fechaObj != null && !fechaObj.toString().isEmpty()) {
                    if (fechaObj instanceof java.sql.Date) {
                        FECHASISTENCIAA.setDate(new java.util.Date(((java.sql.Date) fechaObj).getTime()));
                    } else if (fechaObj instanceof java.util.Date) {
                        FECHASISTENCIAA.setDate((java.util.Date) fechaObj);
                    } else {
                        // Si es String, intentar parsear
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        FECHASISTENCIAA.setDate(sdf.parse(fechaObj.toString()));
                    }
                } else {
                    FECHASISTENCIAA.setDate(new java.util.Date()); // Fecha actual por defecto
                }
            } catch (Exception e) {
                System.out.println("Error al cargar fecha: " + e);
                FECHASISTENCIAA.setDate(new java.util.Date());
            }

            // Cargar horas (pueden ser null)
            Object horaEntrada = TABLASISTENCIA.getValueAt(fila, 4);
            Object horaSalida = TABLASISTENCIA.getValueAt(fila, 5);

            if (horaEntrada != null && !horaEntrada.toString().isEmpty()) {
                CMBHORAENTRADA.setSelectedItem(horaEntrada.toString());
            } else {
                CMBHORAENTRADA.setSelectedIndex(0); // Primera hora disponible
            }

            if (horaSalida != null && !horaSalida.toString().isEmpty()) {
                CMBHORASALIDA.setSelectedItem(horaSalida.toString());
            } else {
                CMBHORASALIDA.setSelectedIndex(CMBHORASALIDA.getItemCount() - 1); // Última hora
            }
        }
    }//GEN-LAST:event_TABLASISTENCIAMouseClicked

    private void BTNAGREGARASISTENCIAActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BTNAGREGARASISTENCIAActionPerformed
        try {
            if (TXTCLIENTE.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Escribe el nombre del cliente primero.");
                return;
            }

            int idCliente = asis.obtenerIdClientePorNombre(TXTCLIENTE.getText().trim());

            if (idCliente == -1) {
                JOptionPane.showMessageDialog(this, "El cliente '" + TXTCLIENTE.getText() + "' no existe. Regístralo primero.");
                return;
            }

            if (FECHASISTENCIAA.getDate() == null) {
                JOptionPane.showMessageDialog(this, "Selecciona una fecha.");
                return;
            }

            asis.setId_Cliente(idCliente);

            SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
            String fechaTexto = formato.format(FECHASISTENCIAA.getDate());
            asis.setFecha(fechaTexto);

            asis.setHora_Entrada(CMBHORAENTRADA.getSelectedItem().toString());
            asis.setHora_Salida(CMBHORASALIDA.getSelectedItem().toString());

            asis.InsertarAsistencia();

            JOptionPane.showMessageDialog(this, "¡Asistencia registrada exitosamente!");

            cargarTablaAsistencia("");
            limpiarA();

        } catch (Exception e) {
            System.out.println("Error al agregar: " + e);
            JOptionPane.showMessageDialog(this, "Ocurrió un error: " + e.getMessage());
            e.printStackTrace();
        }
    }//GEN-LAST:event_BTNAGREGARASISTENCIAActionPerformed

    private void BTNCONSULTARASISTENCIAAActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BTNCONSULTARASISTENCIAAActionPerformed
        cargarTablaAsistencia("");
        limpiarA();
    }//GEN-LAST:event_BTNCONSULTARASISTENCIAAActionPerformed

    private void BTNEDITARAASISTENCIAActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BTNEDITARAASISTENCIAActionPerformed
        try {
            // Verificar si hay un ID de asistencia
            boolean esNuevaAsistencia = TXTIDASISTENCIA.getText().trim().isEmpty();

            String nombreCliente = TXTCLIENTE.getText().trim();
            if (nombreCliente.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Seleccione un cliente de la tabla");
                return;
            }

            int idCliente = asis.obtenerIdClientePorNombre(nombreCliente);
            if (idCliente == -1) {
                JOptionPane.showMessageDialog(this, "No se encontró el cliente '" + nombreCliente + "'");
                return;
            }

            if (FECHASISTENCIAA.getDate() == null) {
                JOptionPane.showMessageDialog(this, "Seleccione una fecha");
                return;
            }

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String fecha = sdf.format(FECHASISTENCIAA.getDate());
            String horaEntrada = CMBHORAENTRADA.getSelectedItem().toString();
            String horaSalida = CMBHORASALIDA.getSelectedItem().toString();

            if (esNuevaAsistencia) {
                // INSERTAR nueva asistencia
                asis.setId_Cliente(idCliente);
                asis.setFecha(fecha);
                asis.setHora_Entrada(horaEntrada);
                asis.setHora_Salida(horaSalida);
                asis.InsertarAsistencia();
                JOptionPane.showMessageDialog(this, "Nueva asistencia registrada correctamente");
            } else {
                // ACTUALIZAR asistencia existente
                asis.setId_Asistencia(Integer.parseInt(TXTIDASISTENCIA.getText()));
                asis.setId_Cliente(idCliente);
                asis.setFecha(fecha);
                asis.setHora_Entrada(horaEntrada);
                asis.setHora_Salida(horaSalida);
                asis.ActualizarAsistencia();
                JOptionPane.showMessageDialog(this, "Asistencia actualizada correctamente");
            }

            cargarTablaAsistencia("");
            limpiarA();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al editar: " + e.getMessage());
            e.printStackTrace();
        }
    }//GEN-LAST:event_BTNEDITARAASISTENCIAActionPerformed

    private void BTNELIMINARASISTENCIAActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BTNELIMINARASISTENCIAActionPerformed
        try {
            int fila = TABLASISTENCIA.getSelectedRow();
            if (fila == -1) {
                return;
            }

            int idAsistencia = Integer.parseInt(TABLASISTENCIA.getValueAt(fila, 0).toString());
            asis.setId_Asistencia(idAsistencia);
            asis.EliminarAsistencia();

            cargarTablaAsistencia("");
            limpiarA();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al eliminar: " + e);
        }

    }//GEN-LAST:event_BTNELIMINARASISTENCIAActionPerformed

    private void TXTBUSCARASISTENCIAKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TXTBUSCARASISTENCIAKeyReleased

        cargarTablaAsistencia(TXTCLIENTE.getText().trim());
    }//GEN-LAST:event_TXTBUSCARASISTENCIAKeyReleased

    private void BTNAGREGARCPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BTNAGREGARCPActionPerformed

        try {
            cp.setId_Cliente(obtenerIdCliente());
            cp.setId_Plan(obtenerIdPlan());

            // Obtener fechas del JDateChooser
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            cp.setFecha_Inicio(sdf.format(FECHAINICIO.getDate()));
            cp.setFecha_Final(sdf.format(FECHAFINAL.getDate()));

            // Estado
            cp.setEstado(CMBESTADO.getSelectedItem().toString());

            cp.InsertarClientePlan();

            JOptionPane.showMessageDialog(this, "REGISTRO INSERTADO");
            cargarTablaCP("");
            limpiarPC();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al insertar: " + e);
        }

    }//GEN-LAST:event_BTNAGREGARCPActionPerformed

    private void TABLA_CLIENTE_PLANMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TABLA_CLIENTE_PLANMouseClicked
        int fila = TABLA_CLIENTE_PLAN.getSelectedRow();
        if (fila != -1) {
            // ID Cliente Plan (para editar/eliminar)
            TXTIDCLIENTE_PLAN.setText(TABLA_CLIENTE_PLAN.getValueAt(fila, 0).toString());

            // Seleccionar Cliente
            String idCliente = TABLA_CLIENTE_PLAN.getValueAt(fila, 1).toString();
            seleccionarItemComboxpc(CMBCLIENTE, idCliente);

            // Seleccionar Plan
            String idPlan = TABLA_CLIENTE_PLAN.getValueAt(fila, 3).toString();
            seleccionarItemComboxpc(CMBPLAN, idPlan);

            // Fechas
            try {
                FECHAINICIO.setDate((java.util.Date) TABLA_CLIENTE_PLAN.getValueAt(fila, 5));
                FECHAFINAL.setDate((java.util.Date) TABLA_CLIENTE_PLAN.getValueAt(fila, 6));
            } catch (Exception e) {
                System.out.println("Error al cargar fechas: " + e);
            }

            // Estado
            CMBESTADO.setSelectedItem(TABLA_CLIENTE_PLAN.getValueAt(fila, 7).toString());
        }

    }//GEN-LAST:event_TABLA_CLIENTE_PLANMouseClicked

    private void BTNCONSULTARCPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BTNCONSULTARCPActionPerformed
        cargarTablaCP("");
        limpiarPC();
    }//GEN-LAST:event_BTNCONSULTARCPActionPerformed

    private void BTNEDITARCPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BTNEDITARCPActionPerformed
        try {
            if (TXTIDCLIENTE_PLAN.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Seleccione un registro de la tabla");
                return;
            }

            cp.setId_Cliente_Plan(Integer.parseInt(TXTIDCLIENTE_PLAN.getText()));
            cp.setId_Cliente(obtenerIdCliente());
            cp.setId_Plan(obtenerIdPlan());

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            cp.setFecha_Inicio(sdf.format(FECHAINICIO.getDate()));
            cp.setFecha_Final(sdf.format(FECHAFINAL.getDate()));
            cp.setEstado(CMBESTADO.getSelectedItem().toString());

            cp.ActualizarClientePlan();

            JOptionPane.showMessageDialog(this, "Plan de cliente actualizado");
            cargarTablaCP("");
            limpiarPC();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al actualizar: " + e.getMessage());
            e.printStackTrace();
        }
    }//GEN-LAST:event_BTNEDITARCPActionPerformed

    private void BTNELIMINARCPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BTNELIMINARCPActionPerformed
        try {
            int fila = TABLA_CLIENTE_PLAN.getSelectedRow();
            if (fila == -1) {
                JOptionPane.showMessageDialog(this, "Seleccione un registro de la tabla.");
                return;
            }

            // Obtener el IDPLANCLI desde la tabla (columna 0)
            int idPlanCli = Integer.parseInt(TABLA_CLIENTE_PLAN.getValueAt(fila, 0).toString());

            // Confirmación
            int opc = JOptionPane.showConfirmDialog(
                    this,
                    "¿Seguro que desea eliminar este registro?",
                    "Confirmar eliminación",
                    JOptionPane.YES_NO_OPTION
            );

            if (opc != JOptionPane.YES_OPTION) {
                return;
            }

            // Ejecutar borrado
            String sql = "DELETE FROM cliente_plan WHERE IDCLIENTEPLAN = " + idPlanCli;
            cp.bd.ActualizarBD(sql);

            JOptionPane.showMessageDialog(this, "Registro eliminado correctamente.");

            // Recargar tabla y limpiar controles
            cargarTablaCP("");
            limpiarPC();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al eliminar: " + e);
        }
    }//GEN-LAST:event_BTNELIMINARCPActionPerformed

    private void TXTBUSCARCPKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TXTBUSCARCPKeyReleased

        cargarTablaCP(TXTBUSCARCP.getText());

    }//GEN-LAST:event_TXTBUSCARCPKeyReleased

    private void TABLAUSOMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TABLAUSOMouseClicked
        int fila = TABLAUSO.getSelectedRow();
        if (fila != -1) {
            try {
                // ID
                TXTIDUSO.setText(TABLAUSO.getValueAt(fila, 0).toString());

                // Cliente
                String idCliente = TABLAUSO.getValueAt(fila, 1).toString();
                seleccionarItemComboxU(CMBCLIENTE1, idCliente);

                // Equipo
                String idEquipo = TABLAUSO.getValueAt(fila, 3).toString();
                seleccionarItemComboxU(CMBEQUIPO, idEquipo);

                // Fecha
                java.util.Date fecha = (java.util.Date) TABLAUSO.getValueAt(fila, 5);
                FECHAUSO.setDate(fecha);

                // Tiempo - convertir el texto formateado de vuelta a minutos
                String tiempoTexto = TABLAUSO.getValueAt(fila, 6).toString();
                int minutos = convertirTextoAMinutos(tiempoTexto);

                // Seleccionar en el combo el tiempo más cercano
                String tiempoCombo = formatMinutosToLabel(minutos);
                CMBTIEMPO.setSelectedItem(tiempoCombo);

            } catch (Exception e) {
                System.out.println("Error al cargar datos: " + e);
                e.printStackTrace();
            }
        }
    }//GEN-LAST:event_TABLAUSOMouseClicked

    private void BTNEDITARUSOActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BTNEDITARUSOActionPerformed
        try {
            if (TXTIDUSO.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Seleccione un registro.");
                return;
            }

            int idUso = Integer.parseInt(TXTIDUSO.getText());
            int idCliente = Integer.parseInt(CMBCLIENTE1.getSelectedItem().toString().split(" - ")[0]);
            int idEquipo = Integer.parseInt(CMBEQUIPO.getSelectedItem().toString().split(" - ")[0]);

            if (FECHAUSO.getDate() == null) {
                JOptionPane.showMessageDialog(this, "Seleccione una fecha.");
                return;
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String fechaStr = sdf.format(FECHAUSO.getDate());

            String tiempoItem = CMBTIEMPO.getSelectedItem().toString();
            int minutos = convertirTiempoAMinutos(tiempoItem);

            String sql = "UPDATE uso_equipo SET IDCLIENTE = " + idCliente
                    + ", IDEQUIPO = " + idEquipo
                    + ", FECHA = '" + fechaStr + "'"
                    + ", TIEMPO = " + minutos
                    + " WHERE IDUSO = " + idUso;

            uso.bd.ActualizarBD(sql);
            JOptionPane.showMessageDialog(this, "Registro actualizado.");

            cargarTablaUsoEquipo("");
            limpiarU();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al actualizar: " + e);
            e.printStackTrace();
        }
    }//GEN-LAST:event_BTNEDITARUSOActionPerformed

    private void BTNELIMINARUSOActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BTNELIMINARUSOActionPerformed
        try {
            if (TXTIDUSO.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Seleccione un registro.");
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this,
                    "¿Eliminar registro?", "Confirmar", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                uso.setId_Uso(Integer.parseInt(TXTIDUSO.getText()));
                uso.EliminarUso_Equipo();

                JOptionPane.showMessageDialog(this, "Registro eliminado.");
                cargarTablaUsoEquipo("");
                limpiarU();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al eliminar: " + e.getMessage());
            e.printStackTrace();
        }
    }//GEN-LAST:event_BTNELIMINARUSOActionPerformed

    private void BTNAGREGARUSOActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BTNAGREGARUSOActionPerformed
        try {
            if (CMBCLIENTE1.getSelectedIndex() == -1 || CMBEQUIPO.getSelectedIndex() == -1) {
                JOptionPane.showMessageDialog(this, "Seleccione cliente y equipo.");
                return;
            }

            if (CMBTIEMPO.getSelectedIndex() == -1) {
                JOptionPane.showMessageDialog(this, "Seleccione el tiempo.");
                return;
            }

            int idCliente = Integer.parseInt(CMBCLIENTE1.getSelectedItem().toString().split(" - ")[0]);
            int idEquipo = Integer.parseInt(CMBEQUIPO.getSelectedItem().toString().split(" - ")[0]);

            if (FECHAUSO.getDate() == null) {
                JOptionPane.showMessageDialog(this, "Seleccione una fecha.");
                return;
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String fechaStr = sdf.format(FECHAUSO.getDate());

            String tiempoItem = CMBTIEMPO.getSelectedItem().toString();
            int minutos = convertirTiempoAMinutos(tiempoItem);

            uso.setId_Cliente(idCliente);
            uso.setId_Equipo(idEquipo);
            uso.setFecha(fechaStr);
            uso.setTiempo(minutos);

            uso.InsertarUso_Equipo();

            JOptionPane.showMessageDialog(this, "Registro guardado correctamente.");

            limpiarU();
            cargarTablaUsoEquipo("");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al guardar uso equipo: " + e);
            e.printStackTrace();
        }
    }//GEN-LAST:event_BTNAGREGARUSOActionPerformed

    private void TXTBUSCARUSOKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TXTBUSCARUSOKeyReleased

    }//GEN-LAST:event_TXTBUSCARUSOKeyReleased

    private void BTNCONSULTARPLANActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BTNCONSULTARPLANActionPerformed
        cargarTablaPlan("");
        limpiarP();

    }//GEN-LAST:event_BTNCONSULTARPLANActionPerformed

    private void BTNCONSULTARUSOActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BTNCONSULTARUSOActionPerformed
        cargarTablaUsoEquipo("");
        limpiarU();
    }//GEN-LAST:event_BTNCONSULTARUSOActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Frm_Menu_A.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Frm_Menu_A.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Frm_Menu_A.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Frm_Menu_A.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Frm_Menu_A().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BTNAGREGARASISTENCIA;
    private javax.swing.JButton BTNAGREGARCP;
    private javax.swing.JButton BTNAGREGARENTRENADOR;
    private javax.swing.JButton BTNAGREGARPLAN;
    private javax.swing.JButton BTNAGREGARUSO;
    private javax.swing.JButton BTNAGREGAR_EQUIIPO;
    private javax.swing.JButton BTNCONSULTARASISTENCIAA;
    private javax.swing.JButton BTNCONSULTARCP;
    private javax.swing.JButton BTNCONSULTARENTRENADOR;
    private javax.swing.JButton BTNCONSULTAREQUIPO;
    private javax.swing.JButton BTNCONSULTARPLAN;
    private javax.swing.JButton BTNCONSULTARUSO;
    private javax.swing.JButton BTNEDITARAASISTENCIA;
    private javax.swing.JButton BTNEDITARCP;
    private javax.swing.JButton BTNEDITARENTRENADOR;
    private javax.swing.JButton BTNEDITAREQUIPO;
    private javax.swing.JButton BTNEDITARPLAN;
    private javax.swing.JButton BTNEDITARUSO;
    private javax.swing.JButton BTNELIMINARASISTENCIA;
    private javax.swing.JButton BTNELIMINARCP;
    private javax.swing.JButton BTNELIMINARENTRENADOR;
    private javax.swing.JButton BTNELIMINAREQUIPO;
    private javax.swing.JButton BTNELIMINARPLAN;
    private javax.swing.JButton BTNELIMINARUSO;
    private com.toedter.calendar.JDateChooser CALENDARIOCLIENTE;
    private javax.swing.JComboBox<String> CMBCLIENTE;
    private javax.swing.JComboBox<String> CMBCLIENTE1;
    private javax.swing.JComboBox<String> CMBCOSTO;
    private javax.swing.JComboBox<String> CMBDURACION;
    private javax.swing.JComboBox<String> CMBENTRENADOR;
    private javax.swing.JComboBox<String> CMBEQUIPO;
    private javax.swing.JComboBox<String> CMBESTADO;
    private javax.swing.JComboBox<String> CMBESTADO_EQUIPO1;
    private javax.swing.JComboBox<String> CMBHORAENTRADA;
    private javax.swing.JComboBox<String> CMBHORASALIDA;
    private javax.swing.JComboBox<String> CMBPLAN;
    private javax.swing.JComboBox<String> CMBTIEMPO;
    private com.toedter.calendar.JDateChooser FECHAFINAL;
    private com.toedter.calendar.JDateChooser FECHAINICIO;
    private com.toedter.calendar.JDateChooser FECHASISTENCIAA;
    private com.toedter.calendar.JDateChooser FECHAUSO;
    private javax.swing.JLabel Lfondo;
    private javax.swing.JTable TABLACLIENTE;
    private javax.swing.JTable TABLAENTRENADOR;
    private javax.swing.JTable TABLAEQUIPO;
    private javax.swing.JTable TABLAPLAN;
    private javax.swing.JTable TABLASISTENCIA;
    private javax.swing.JTable TABLAUSO;
    private javax.swing.JTable TABLA_CLIENTE_PLAN;
    private javax.swing.JTextArea TXDESCRIPCION;
    private javax.swing.JTextField TXTAPELLIDOSCLIENTE;
    private javax.swing.JTextField TXTBUSCARASISTENCIA;
    private javax.swing.JTextField TXTBUSCARCLIENTE;
    private javax.swing.JTextField TXTBUSCARCP;
    private javax.swing.JTextField TXTBUSCARENTRENADOR;
    private javax.swing.JTextField TXTBUSCAREQUIPO;
    private javax.swing.JTextField TXTBUSCARPLAN;
    private javax.swing.JTextField TXTBUSCARUSO;
    private javax.swing.JTextField TXTCELULAR;
    private javax.swing.JTextField TXTCELULARCLIENTES;
    private javax.swing.JTextField TXTCLIENTE;
    private javax.swing.JTextField TXTCORREO;
    private javax.swing.JTextField TXTDESCRIPCION;
    private javax.swing.JTextField TXTDIRECCIONCLIENTES;
    private javax.swing.JTextField TXTEQUIPO;
    private javax.swing.JTextField TXTESPECIALIDAD;
    private javax.swing.JTextField TXTIDASISTENCIA;
    private javax.swing.JTextField TXTIDCLIENTE_PLAN;
    private javax.swing.JTextField TXTIDENTRENADOR;
    private javax.swing.JTextField TXTIDPLAN;
    private javax.swing.JTextField TXTIDUSO;
    private javax.swing.JTextField TXTIPO;
    private javax.swing.JTextField TXTNAMEEQUIPO;
    private javax.swing.JTextField TXTNOMBREPLAN;
    private javax.swing.JTextField TXTNOMBRESCLIENTES;
    private javax.swing.JTextField TXTNOMBRETRADOR;
    private javax.swing.JButton btnAgregar;
    private javax.swing.JButton btnConsultar;
    private javax.swing.JButton btnEditar;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel54;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenu jMenu5;
    private javax.swing.JMenu jMenu6;
    private javax.swing.JMenu jMenu7;
    private javax.swing.JMenu jMenu8;
    private javax.swing.JMenuBar jMenuBar2;
    private javax.swing.JMenuBar jMenuBar3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextField txtIDCliente;
    // End of variables declaration//GEN-END:variables
}
