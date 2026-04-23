package br.esports.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;

public class Icones {

    public static ImageIcon mais(int size, Color cor) {
        BufferedImage img = novo(size);
        Graphics2D g = base(img);
        g.setColor(cor);
        g.setStroke(new BasicStroke(size * 0.14f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        int m = size / 4, c = size / 2;
        g.drawLine(c, m, c, size - m);
        g.drawLine(m, c, size - m, c);
        g.dispose();
        return new ImageIcon(img);
    }

    public static ImageIcon lapis(int size, Color cor) {
        BufferedImage img = novo(size);
        Graphics2D g = base(img);
        g.setColor(cor);
        g.setStroke(new BasicStroke(size * 0.12f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        int p = size / 6;
        int[] xs = {p, size - p - p, size - p, p + p};
        int[] ys = {size - p, p, p + p, size - p + p};
        g.drawPolygon(xs, ys, 4);
        g.drawLine(size - p - p, p, p + p, size - p + p);
        g.dispose();
        return new ImageIcon(img);
    }

    public static ImageIcon lixeira(int size, Color cor) {
        BufferedImage img = novo(size);
        Graphics2D g = base(img);
        g.setColor(cor);
        g.setStroke(new BasicStroke(size * 0.11f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        int p = size / 5;
        // corpo
        g.drawRoundRect(p, p + p / 2, size - p * 2, size - p - p / 2, 4, 4);
        // tampa
        g.drawLine(p - p / 3, p + p / 2, size - p + p / 3, p + p / 2);
        // alça
        g.drawLine(size / 2 - p / 2, p + p / 2, size / 2 - p / 2, p);
        g.drawLine(size / 2 + p / 2, p + p / 2, size / 2 + p / 2, p);
        g.drawLine(size / 2 - p / 2, p, size / 2 + p / 2, p);
        // linhas internas
        int bx = p + (size - p * 2) / 4;
        g.drawLine(bx, p + p, bx, size - p - 2);
        g.drawLine(size / 2, p + p, size / 2, size - p - 2);
        g.drawLine(size - bx, p + p, size - bx, size - p - 2);
        g.dispose();
        return new ImageIcon(img);
    }

    public static ImageIcon vassoura(int size, Color cor) {
        BufferedImage img = novo(size);
        Graphics2D g = base(img);
        g.setColor(cor);
        g.setStroke(new BasicStroke(size * 0.12f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        int p = size / 5;
        // cabo
        g.drawLine(size - p, p, p + 2, size - p - 2);
        // cabeça da vassoura
        g.drawLine(p, size - p, p + p, size - p + 2);
        g.drawLine(p + p, size - p + 2, p + p + 2, size - p - p);
        g.drawLine(p + p + 2, size - p - p, p, size - p);
        g.dispose();
        return new ImageIcon(img);
    }

    public static ImageIcon seta(int size, Color cor) {
        BufferedImage img = novo(size);
        Graphics2D g = base(img);
        g.setColor(cor);
        g.setStroke(new BasicStroke(size * 0.13f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        int c = size / 2, r = size / 3;
        // arco
        g.drawArc(c - r, c - r, r * 2, r * 2, 30, 270);
        // ponta da seta
        int ax = c + r, ay = c;
        g.drawLine(ax, ay - r / 2, ax, ay);
        g.drawLine(ax - r / 2, ay, ax, ay);
        g.dispose();
        return new ImageIcon(img);
    }

    // ── utilitários ────────────────────────────────────────────
    private static BufferedImage novo(int size) {
        return new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
    }

    private static Graphics2D base(BufferedImage img) {
        Graphics2D g = img.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,  RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        return g;
    }
}
