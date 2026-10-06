package com.aerolineavs.tarifas;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Objects;
import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SwingConstants;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;

/**
 * Ventana Swing para consultar tarifas sin acoplar la interfaz a su implementación.
 */
public final class PricingSwingApp extends JFrame {

    private static final Color CANVAS = new Color(245, 248, 247);
    private static final Color ACCENT = new Color(13, 105, 91);

    private final IPricingService pricingService;
    private final JSpinner ageSpinner = new JSpinner(new SpinnerNumberModel(30, 0, 120, 1));
    private final JSpinner flightsSpinner = new JSpinner(new SpinnerNumberModel(6, 0, 10000, 1));
    private final JSpinner incomeSpinner = new JSpinner(
            new SpinnerNumberModel(25000.0, 0.0, 100000000.0, 500.0));
    private final JComboBox<TipoViajero> travelerTypeCombo = createCombo(TipoViajero.values());
    private final JComboBox<ClaseVuelo> cabinCombo = createCombo(ClaseVuelo.values());
    private final JComboBox<RegionDestino> destinationCombo = createCombo(RegionDestino.values());
    private final JCheckBox childrenCheckBox = new JCheckBox("Viaja con niños menores de 12 años");
    private final JCheckBox parentsCheckBox = new JCheckBox("Vive con sus padres");
    private final JLabel fareValue = new JLabel("Sin calcular");
    private final JLabel discountValue = new JLabel("-", SwingConstants.RIGHT);

    public PricingSwingApp(IPricingService pricingService) {
        super("AerolineaVS | Consulta de tarifas");
        this.pricingService = Objects.requireNonNull(pricingService, "pricingService");
        configureWindow();
        setContentPane(createContent());
    }

    private void configureWindow() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(620, 650));
        setSize(720, 720);
        setLocationRelativeTo(null);
        incomeSpinner.setEditor(new JSpinner.NumberEditor(incomeSpinner, "#,##0.00"));
    }

    private JPanel createContent() {
        JPanel content = new JPanel(new BorderLayout(0, 18));
        content.setBackground(CANVAS);
        content.setBorder(new EmptyBorder(26, 30, 26, 30));

        JLabel title = new JLabel("Consulta de tarifas");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 25f));
        title.setForeground(new Color(29, 48, 45));
        content.add(title, BorderLayout.NORTH);

        JPanel body = new JPanel(new BorderLayout(0, 16));
        body.setOpaque(false);
        body.add(createProfileForm(), BorderLayout.NORTH);
        body.add(createResultPanel(), BorderLayout.CENTER);
        body.add(createActions(), BorderLayout.SOUTH);
        content.add(body, BorderLayout.CENTER);
        return content;
    }

    private JPanel createProfileForm() {
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE);
        form.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(218, 226, 223)),
                new EmptyBorder(12, 18, 12, 18)));

        addRow(form, 0, "Edad", ageSpinner);
        addRow(form, 1, "Vuelos al año", flightsSpinner);
        addRow(form, 2, "Tipo de viajero", travelerTypeCombo);
        addRow(form, 3, "Clase preferida", cabinCombo);
        addRow(form, 4, "Destino preferido", destinationCombo);
        addRow(form, 5, "Ingresos anuales (€)", incomeSpinner);
        addRow(form, 6, "", childrenCheckBox);
        addRow(form, 7, "", parentsCheckBox);
        return form;
    }

    private JPanel createResultPanel() {
        JPanel result = new JPanel(new GridBagLayout());
        result.setBackground(Color.WHITE);
        result.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(218, 226, 223)),
                new EmptyBorder(14, 18, 14, 18)));

        GridBagConstraints labelConstraints = new GridBagConstraints();
        labelConstraints.gridx = 0;
        labelConstraints.gridy = 0;
        labelConstraints.anchor = GridBagConstraints.WEST;
        labelConstraints.insets = new Insets(4, 0, 8, 12);
        JLabel fareLabel = new JLabel("Tarifa recomendada");
        result.add(fareLabel, labelConstraints);

        GridBagConstraints fareConstraints = new GridBagConstraints();
        fareConstraints.gridx = 1;
        fareConstraints.gridy = 0;
        fareConstraints.weightx = 1;
        fareConstraints.fill = GridBagConstraints.HORIZONTAL;
        fareConstraints.anchor = GridBagConstraints.EAST;
        fareConstraints.insets = new Insets(4, 0, 8, 0);
        fareValue.setHorizontalAlignment(SwingConstants.RIGHT);
        fareValue.setFont(fareValue.getFont().deriveFont(Font.BOLD, 19f));
        fareValue.setForeground(ACCENT);
        result.add(fareValue, fareConstraints);

        GridBagConstraints discountLabelConstraints = new GridBagConstraints();
        discountLabelConstraints.gridx = 0;
        discountLabelConstraints.gridy = 1;
        discountLabelConstraints.anchor = GridBagConstraints.WEST;
        discountLabelConstraints.insets = new Insets(4, 0, 4, 12);
        result.add(new JLabel("Descuento"), discountLabelConstraints);

        GridBagConstraints discountConstraints = new GridBagConstraints();
        discountConstraints.gridx = 1;
        discountConstraints.gridy = 1;
        discountConstraints.weightx = 1;
        discountConstraints.fill = GridBagConstraints.HORIZONTAL;
        discountConstraints.anchor = GridBagConstraints.EAST;
        discountConstraints.insets = new Insets(4, 0, 4, 0);
        discountValue.setHorizontalAlignment(SwingConstants.RIGHT);
        discountValue.setFont(discountValue.getFont().deriveFont(Font.BOLD, 19f));
        result.add(discountValue, discountConstraints);
        return result;
    }

    private JPanel createActions() {
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        actions.setOpaque(false);

        JButton calculateButton = new JButton("Calcular tarifa");
        calculateButton.setBackground(ACCENT);
        calculateButton.setForeground(Color.WHITE);
        calculateButton.setFocusPainted(false);
        calculateButton.addActionListener(event -> calculateFare());
        actions.add(calculateButton);
        getRootPane().setDefaultButton(calculateButton);
        return actions;
    }

    private void calculateFare() {
        ClientePotencial cliente = new ClientePotencial(
                ((Number) ageSpinner.getValue()).intValue(),
                ((Number) flightsSpinner.getValue()).intValue(),
                (TipoViajero) travelerTypeCombo.getSelectedItem(),
                (ClaseVuelo) cabinCombo.getSelectedItem(),
                (RegionDestino) destinationCombo.getSelectedItem(),
                ((Number) incomeSpinner.getValue()).doubleValue(),
                childrenCheckBox.isSelected(),
                parentsCheckBox.isSelected()
        );

        try {
            ResultadoTarifa resultado = pricingService.evaluar(cliente);
            fareValue.setText(resultado.tarifa().getNombre());
            discountValue.setText(resultado.tarifa().getDescuentoPorcentaje() + "%");
        } catch (RuntimeException exception) {
            JOptionPane.showMessageDialog(
                    this,
                    "No se pudo calcular la tarifa. Inténtelo de nuevo.",
                    "Error de cálculo",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private static void addRow(JPanel form, int row, String label, Component component) {
        GridBagConstraints labelConstraints = new GridBagConstraints();
        labelConstraints.gridx = 0;
        labelConstraints.gridy = row;
        labelConstraints.anchor = GridBagConstraints.WEST;
        labelConstraints.insets = new Insets(5, 2, 5, 16);
        form.add(new JLabel(label), labelConstraints);

        GridBagConstraints inputConstraints = new GridBagConstraints();
        inputConstraints.gridx = 1;
        inputConstraints.gridy = row;
        inputConstraints.weightx = 1;
        inputConstraints.fill = GridBagConstraints.HORIZONTAL;
        inputConstraints.insets = new Insets(5, 0, 5, 2);
        if (component instanceof JCheckBox checkBox) {
            checkBox.setOpaque(false);
        } else {
            component.setPreferredSize(new Dimension(280, 30));
        }
        form.add(component, inputConstraints);
    }

    private static <E extends Enum<E>> JComboBox<E> createCombo(E[] values) {
        JComboBox<E> combo = new JComboBox<>(values);
        combo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(
                    JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                return super.getListCellRendererComponent(
                        list, displayName(value), index, isSelected, cellHasFocus);
            }
        });
        combo.setPreferredSize(new Dimension(280, 30));
        return combo;
    }

    private static String displayName(Object value) {
        if (value instanceof TipoViajero tipo) {
            return switch (tipo) {
                case MENOR -> "Menor";
                case ESTUDIANTE_UNIVERSITARIO_DESPLAZADO -> "Estudiante universitario desplazado";
                case TRABAJADOR_JOVEN -> "Trabajador joven";
                case GENERAL -> "General";
            };
        }
        if (value instanceof ClaseVuelo clase) {
            return clase == ClaseVuelo.TURISTA ? "Turista" : "Business";
        }
        if (value instanceof RegionDestino destino) {
            return switch (destino) {
                case EUROPA -> "Europa";
                case ASIA -> "Asia";
                case AMERICA -> "América";
                case OTRA -> "Otra";
            };
        }
        return String.valueOf(value);
    }
}