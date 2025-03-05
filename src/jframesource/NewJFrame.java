/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package jframesource;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import org.json.simple.JSONArray; 
import org.json.simple.JSONObject; 
import org.json.simple.parser.*;
/**
 *
 * @author user
 */
public class NewJFrame extends javax.swing.JFrame {
    
    private ArrayList<RecIntegral> data = new ArrayList<>();
    
    JFileChooser fileChooser = new JFileChooser();
    
    // PopupMenu Load
    JPopupMenu loadPopUp = new JPopupMenu();
    JMenuItem pmLoad1 = new JMenuItem("Текстовый");
    JMenuItem pmLoad2 = new JMenuItem("Двоичный");
    
    JPopupMenu savePopUp = new JPopupMenu();
    JMenuItem pmSave1 = new JMenuItem("Текстовый");
    JMenuItem pmSave2 = new JMenuItem("Двоичный");
    JMenuItem pmSave3 = new JMenuItem("JSON");
    
    void Read(FileReader reader, String path) { 
        
        char[] buffer = new char[2048];
        try {
            reader.read(buffer);
        } catch (Exception e) {return;}
        
        DefaultTableModel model = (javax.swing.table.DefaultTableModel)jTable1.getModel();
        model.setRowCount(0);
        data.clear();
        String temp = String.copyValueOf(buffer);
        String datas = String.copyValueOf(buffer, 0, temp.indexOf('\0'));
        //char symbol = buffer[202];
        
        if (path.endsWith(".json")) {
            try {
                JSONParser parser = new JSONParser();
                Object obj = parser.parse(datas);
                JSONObject jsonObject = (JSONObject) obj;
            
                var jsonArray = (JSONArray) jsonObject.get("results");
                var it = jsonArray.iterator();

                while (it.hasNext()) {
                    var result = (JSONObject) it.next();
                    String low = result.get("low").toString(),
                            high = result.get("high").toString(),
                            step = result.get("step").toString(),
                            res = result.get("result").toString();
                    try {
                        RecIntegral info = new RecIntegral(Double.parseDouble(low),
                                Double.parseDouble(high),
                                Double.parseDouble(step));
                        info.result = Double.parseDouble(res);

                        data.add(info);
                        //model.addRow(new Object[] {info.low, info.high, info.step, res});
                    } catch (NewException ex) {
                        Logger.getLogger(NewJFrame.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            } catch (ParseException ex) {
                Logger.getLogger(NewJFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
            jButton4MouseClicked(null);
        }
        else {
            
            String[] rows = datas.split("\n");
            for (String row : rows) {
                if (row.isEmpty()) break;
                try {
                    String[] values = row.split("\t");

                    RecIntegral info = new RecIntegral(Double.parseDouble(values[0]),
                            Double.parseDouble(values[1]),
                            Double.parseDouble(values[2]));

                    data.add(info);
                    model.addRow(new Object[] {info.low, info.high, info.step, values[3]});
                } catch (NewException ex) {
                    Logger.getLogger(NewJFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        
    }
    
    void Read(ObjectInputStream reader) {
        String buffer = new String();
        try {
            buffer = (String)reader.readObject();
        } catch (Exception e) {return;}
        
        DefaultTableModel model = (javax.swing.table.DefaultTableModel)jTable1.getModel();
        model.setRowCount(0);
        
        String[] rows = buffer.split("\n");
        for (String row : rows) {
            if (row.isEmpty()) break;
            try {
                String[] values = row.split("\t");
                
                RecIntegral info = new RecIntegral(Double.parseDouble(values[0]),
                        Double.parseDouble(values[1]),
                        Double.parseDouble(values[2]));
                
                data.add(info);
                model.addRow(new Object[] {info.low, info.high, info.step, values[3]});
            } catch (NewException ex) {
                Logger.getLogger(NewJFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    void Write(FileWriter writer) {
        String buffer = new String();
        
        for (RecIntegral row : data) {
            buffer += Double.toString(row.low) + "\t"
                    + Double.toString(row.high) + "\t"
                    + Double.toString(row.step) + "\t"
                    + Double.toString(row.result) + "\n";
        }
        try {
            writer.write(buffer);
        } catch (Exception e) {
            new NewException(e.getLocalizedMessage());
        }
        try {
            writer.close();
        } catch (Exception e) {}
    }
    
    void Write(ObjectOutputStream writer) {
        String buffer = new String();
        
        for (RecIntegral row : data) {
            buffer += Double.toString(row.low) + "\t"
                    + Double.toString(row.high) + "\t"
                    + Double.toString(row.step) + "\t"
                    + Double.toString(row.result) + "\n";
        }
        
        try {
            writer.writeObject(buffer);
        } catch (Exception e) {
            new NewException(e.getLocalizedMessage());
        }
        try {
            writer.close();
        } catch (Exception e) {}
    }
    
    void WriteJSON(FileWriter writer) {
        var jsonObject = new JSONObject();
        
        var jsonArray = new JSONArray();
        for (RecIntegral line : data) {
            var obj = new JSONObject();
            
            obj.put("low", line.low);
            obj.put("high", line.high);
            obj.put("step", line.step);
            obj.put("result", line.result);
            
            jsonArray.add(obj);
        }
        
        jsonObject.put("results", jsonArray);
        
        try {
            writer.write(jsonObject.toString());
        } catch (Exception e) {
            new NewException(e.getLocalizedMessage());
        }
        try {
            writer.close();
        } catch (Exception e) {}
        //jsonArray
        //var jsonObject = new JSONObject().putAll(jsonArray.)
    }
    /**
     * Creates new form NewJFrame
     */
    public NewJFrame() {
        initComponents();
        
        loadPopUp.add(pmLoad1);
        loadPopUp.add(pmLoad2);
        pmLoad1.addActionListener((ActionEvent e) -> {
            if (fileChooser.showOpenDialog(null) != JFileChooser.APPROVE_OPTION) return;
            
            File file = fileChooser.getSelectedFile();
            
            try {
                Read(new FileReader(file.getAbsolutePath()), file.getAbsolutePath());
            } catch (IOException ex) {
                Logger.getLogger(NewJFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        pmLoad2.addActionListener((ActionEvent e) -> {
            if (fileChooser.showOpenDialog(null) != JFileChooser.APPROVE_OPTION) return;
            
            File file = fileChooser.getSelectedFile();
            
            try {
                Read(new ObjectInputStream(
                        new BufferedInputStream(
                                new FileInputStream(file.getAbsolutePath()))));
            } catch (IOException ex) {
                Logger.getLogger(NewJFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
//        pmLoad1.addActionListener();
//        pmLoad2.addActionListener();

        savePopUp.add(pmSave1);
        savePopUp.add(pmSave2);
        savePopUp.add(pmSave3);
        pmSave1.addActionListener((ActionEvent e) -> {
            if (data.isEmpty()) return;
            if (fileChooser.showSaveDialog(null) != JFileChooser.APPROVE_OPTION) return;
            
            File file = fileChooser.getSelectedFile();
            
            try {
                Write(new FileWriter(file.getAbsolutePath()));
            } catch (IOException ex) {
                Logger.getLogger(NewJFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        pmSave2.addActionListener((ActionEvent e) -> {
            if (data.isEmpty()) return;
            if (fileChooser.showSaveDialog(null) != JFileChooser.APPROVE_OPTION) return;
            
            File file = fileChooser.getSelectedFile();
            
            try {
                Write(new ObjectOutputStream(
                        new BufferedOutputStream(
                                new FileOutputStream(file.getAbsolutePath()))));
            } catch (IOException ex) {
                Logger.getLogger(NewJFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        pmSave3.addActionListener((ActionEvent e) -> {
            if (data.isEmpty()) return;
            if (fileChooser.showSaveDialog(null) != JFileChooser.APPROVE_OPTION) return;
            
            File file = fileChooser.getSelectedFile();
            
            try {
                WriteJSON(new FileWriter(file.getAbsolutePath()));
            } catch (IOException ex) {
                Logger.getLogger(NewJFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
//        pmSave1.addActionListener();
//        pmSave2.addActionListener();
                
        var headerRenderer = new DefaultTableCellRenderer();
        headerRenderer.setBackground(new Color(0,150,150));

        for (int i = 0; i < jTable1.getModel().getColumnCount(); i++)
            jTable1.getColumnModel().getColumn(i).setHeaderRenderer(headerRenderer);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jTextField1 = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(153, 255, 51));
        setCursor(new java.awt.Cursor(java.awt.Cursor.WAIT_CURSOR));
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(0, 167, 166));
        jPanel1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        jTextField1.setBackground(new java.awt.Color(0, 204, 204));
        jTextField1.setToolTipText("Число");

        jLabel2.setBackground(new java.awt.Color(0, 0, 0));
        jLabel2.setFont(new java.awt.Font("Microsoft JhengHei Light", 1, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 102, 102));
        jLabel2.setText("Верхняя граница");
        jLabel2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        jTextField2.setBackground(new java.awt.Color(0, 204, 204));
        jTextField2.setToolTipText("Число");

        jLabel3.setBackground(new java.awt.Color(0, 0, 0));
        jLabel3.setFont(new java.awt.Font("Microsoft JhengHei Light", 1, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(0, 102, 102));
        jLabel3.setText("Шаг");
        jLabel3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        jTextField3.setBackground(new java.awt.Color(0, 204, 204));
        jTextField3.setToolTipText("Число");

        jButton1.setBackground(new java.awt.Color(0, 204, 204));
        jButton1.setFont(new java.awt.Font("Microsoft JhengHei Light", 1, 12)); // NOI18N
        jButton1.setForeground(new java.awt.Color(0, 102, 102));
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jframesource/add.png"))); // NOI18N
        jButton1.setToolTipText("Добавить");
        jButton1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton1MouseClicked(evt);
            }
        });

        jTable1.setBackground(new java.awt.Color(0, 204, 204));
        jTable1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        jTable1.setFont(new java.awt.Font("Microsoft JhengHei Light", 1, 12)); // NOI18N
        jTable1.setForeground(new java.awt.Color(0, 102, 102));
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Ниж. гр.", "Вер. гр.", "Шаг", "Результат"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jScrollPane1.setViewportView(jTable1);
        if (jTable1.getColumnModel().getColumnCount() > 0) {
            jTable1.getColumnModel().getColumn(0).setResizable(false);
            jTable1.getColumnModel().getColumn(1).setResizable(false);
            jTable1.getColumnModel().getColumn(2).setResizable(false);
            jTable1.getColumnModel().getColumn(3).setResizable(false);
        }

        jButton2.setBackground(new java.awt.Color(0, 204, 204));
        jButton2.setFont(new java.awt.Font("Microsoft JhengHei Light", 1, 12)); // NOI18N
        jButton2.setForeground(new java.awt.Color(0, 102, 102));
        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jframesource/delete.png"))); // NOI18N
        jButton2.setToolTipText("Удалить");
        jButton2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton2MouseClicked(evt);
            }
        });

        jButton3.setBackground(new java.awt.Color(0, 204, 204));
        jButton3.setFont(new java.awt.Font("Microsoft JhengHei Light", 1, 12)); // NOI18N
        jButton3.setForeground(new java.awt.Color(0, 102, 102));
        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jframesource/integral.png"))); // NOI18N
        jButton3.setToolTipText("Вычислить");
        jButton3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton3MouseClicked(evt);
            }
        });

        jLabel1.setBackground(new java.awt.Color(0, 0, 0));
        jLabel1.setFont(new java.awt.Font("Microsoft JhengHei Light", 1, 12)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 102, 102));
        jLabel1.setText("Нижняя граница");
        jLabel1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        jLabel4.setFont(new java.awt.Font("Microsoft JhengHei Light", 1, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(0, 102, 102));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("Интеграл от e^x");
        jLabel4.setToolTipText("");

        jButton4.setBackground(new java.awt.Color(0, 204, 204));
        jButton4.setFont(new java.awt.Font("Microsoft JhengHei Light", 1, 12)); // NOI18N
        jButton4.setForeground(new java.awt.Color(0, 102, 102));
        jButton4.setText("Загрузить");
        jButton4.setToolTipText("Загрузить из коллекции");
        jButton4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton4MouseClicked(evt);
            }
        });

        jButton5.setBackground(new java.awt.Color(0, 204, 204));
        jButton5.setFont(new java.awt.Font("Microsoft JhengHei Light", 1, 12)); // NOI18N
        jButton5.setForeground(new java.awt.Color(0, 102, 102));
        jButton5.setText("Очистить");
        jButton5.setToolTipText("Очистить таблицу");
        jButton5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton5MouseClicked(evt);
            }
        });

        jButton6.setBackground(new java.awt.Color(0, 204, 204));
        jButton6.setFont(new java.awt.Font("Microsoft JhengHei Light", 1, 12)); // NOI18N
        jButton6.setForeground(new java.awt.Color(0, 102, 102));
        jButton6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jframesource/load.png"))); // NOI18N
        jButton6.setToolTipText("Загрузить");
        jButton6.setIconTextGap(0);
        jButton6.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButton6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton6MouseClicked(evt);
            }
        });

        jButton7.setBackground(new java.awt.Color(0, 204, 204));
        jButton7.setFont(new java.awt.Font("Microsoft JhengHei Light", 1, 12)); // NOI18N
        jButton7.setForeground(new java.awt.Color(0, 102, 102));
        jButton7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jframesource/save.png"))); // NOI18N
        jButton7.setToolTipText("Сохранить");
        jButton7.setIconTextGap(0);
        jButton7.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButton7.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton7MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(26, 26, 26)
                                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(29, 29, 29)
                                .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 117, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel3)
                                .addGap(16, 16, 16)))
                        .addGap(18, 18, 18)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jButton4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton5))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jButton3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButton2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel2)
                                .addComponent(jLabel1)
                                .addComponent(jLabel3))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(22, 22, 22)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 216, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(43, 43, 43)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jButton4)
                    .addComponent(jButton5))
                .addGap(9, 9, 9))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton4MouseClicked
        DefaultTableModel model = (javax.swing.table.DefaultTableModel)jTable1.getModel();
        
        model.setRowCount(0);
        
        int count = data.size();
        if (count == 0) return;

        for (int i = 0; i < count; i++) {
            // Получение класса из коллекции
            RecIntegral integral = data.get(i);
            model.addRow(new Object[] {integral.low, integral.high, integral.step, integral.result});
        }
    }//GEN-LAST:event_jButton4MouseClicked

    private void jButton3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton3MouseClicked
        DefaultTableModel model = (javax.swing.table.DefaultTableModel)jTable1.getModel();
        
        int index = jTable1.getSelectedRow();

        if (index == -1) return;
        
        RecIntegral integral = data.get(index);
        // Вызов метода из класса для рассчета интеграла
        Thread calculating = new Thread(() -> {
            long startTime = System.nanoTime(), endTime, duration;
            
            double resNum = integral.CalculateResult();
            Object resObj = resNum;
            model.setValueAt(resObj, index, 3);
            
            endTime = System.nanoTime();
            duration = (endTime - startTime)/1000000;
            System.console().flush();
            System.console().printf("%s ms\n", duration);
        });
        calculating.start();
    }//GEN-LAST:event_jButton3MouseClicked

    private void jButton2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton2MouseClicked
        // jTable1
        DefaultTableModel model = (javax.swing.table.DefaultTableModel)jTable1.getModel();

        int index = jTable1.getSelectedRow();

        if (index == -1) return;

        model.removeRow(index);
        // Удаление записи из коллекции.
        data.remove(index);
    }//GEN-LAST:event_jButton2MouseClicked

    private void jButton1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton1MouseClicked
        // jTable1
        DefaultTableModel model = (javax.swing.table.DefaultTableModel)jTable1.getModel();

        String temp = jTextField1.getText();
        if (temp.isEmpty())
        return;
        double low = Double.parseDouble(temp);

        temp = jTextField2.getText();
        if (temp.isEmpty())
        return;
        double high = Double.parseDouble(temp);

        temp = jTextField3.getText();
        if (temp.isEmpty()) return;
        double step = Double.parseDouble(temp);
        
        jTextField1.setText("");
        jTextField2.setText("");
        jTextField3.setText("");
        
//        if (low >= high || step >= high || step <= 0.0) {
//            return;
//        }
        
        // Класс
        RecIntegral info;
        
        // Получение возможного исключения
        try {
            // Добавление записи в коллекцию
            info = new RecIntegral(low, high, step);
        } catch (NewException exception) {
            return;
        }
        
        // Запись
        data.add(info);
        model.addRow(new Object[] {low, high, step, 0});
    }//GEN-LAST:event_jButton1MouseClicked

    private void jButton5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton5MouseClicked
        javax.swing.table.DefaultTableModel model = (javax.swing.table.DefaultTableModel)jTable1.getModel();
        model.setRowCount(0);
        //data.clear();
    }//GEN-LAST:event_jButton5MouseClicked

    private void jButton6MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton6MouseClicked
        loadPopUp.show(evt.getComponent(), evt.getX(), evt.getY());
    }//GEN-LAST:event_jButton6MouseClicked

    private void jButton7MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton7MouseClicked
        savePopUp.show(evt.getComponent(), evt.getX(), evt.getY());
    }//GEN-LAST:event_jButton7MouseClicked

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
            java.util.logging.Logger.getLogger(NewJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(NewJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(NewJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(NewJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */        
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new NewJFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    // End of variables declaration//GEN-END:variables
}
