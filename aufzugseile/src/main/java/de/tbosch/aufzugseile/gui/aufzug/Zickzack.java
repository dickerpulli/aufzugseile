package de.tbosch.aufzugseile.gui.aufzug;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import de.tbosch.aufzugseile.utils.Constants;
import de.tbosch.aufzugseile.utils.Helper;

// TODO: Auto-generated Javadoc
/**
 * The Class Zickzack.
 */
public class Zickzack extends JComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = -170742739764339192L;

	/** The elevator. */
	private Aufzugschacht aufzugschacht;

	/** The image icon. */
	transient private ImageIcon imageIcon;

	private String iconPath;

	/** The omy for dragging. */
	private int omy;

	/** The first move. */
	private boolean firstMove = true;

	/** The y_rel. */
	private double y_rel;

	/**
	 * The Constructor.
	 * 
	 * @param iconpath
	 *            the iconpath
	 * @param aufzugschacht
	 *            the elevator
	 * @param y_rel
	 *            the relational y coordinate in percent (0 - 1)
	 */
	public Zickzack(Aufzugschacht aufzugschacht, double y_rel, String iconpath) {
		this.aufzugschacht = aufzugschacht;
		this.y_rel = y_rel;
		this.iconPath = iconpath;
		imageIcon = new ImageIcon(Helper.getFileURL(iconpath));
		int imageHeight = imageIcon.getIconHeight();
		setLocation(0, (int) (aufzugschacht.getHeight() * y_rel));
		setSize(aufzugschacht.getWidth(), imageHeight);
		initListener();
	}

	/**
	 * Inits the listener.
	 */
	public void initListener() {
		addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseDragged(MouseEvent evt) {
				mouseDraggedZickzack(evt);
			}
		});
		addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				mouseClickedZickzack(evt);
			}
		});
	}

	/**
	 * Mouse clicked zickzack.
	 * 
	 * @param evt
	 *            the evt
	 */
	@SuppressWarnings("serial")
	private void mouseClickedZickzack(MouseEvent evt) {
		if (evt.getButton() == Constants.RIGHT_MOUSE_BUTTON && !aufzugschacht.mainFrameIsAnyButtonSelected) {
			JPopupMenu popup = new JPopupMenu("Label");
			JMenuItem delete = null;
			delete = new JMenuItem(new AbstractAction("L" + Constants.OE_K + "schen", new ImageIcon(Helper.getFileURL(Constants.DELETE_ICON))) {
				public void actionPerformed(ActionEvent e) {
					popupDelete();
				}
			});
			popup.add(delete);

			// shows the menu if it has any entries
			popup.show(this, evt.getX(), evt.getY());
		} else {
			aufzugschacht.mouseClickedArea(evt);
		}
	}

	/**
	 * Popup delete.
	 */
	private void popupDelete() {
		aufzugschacht.deleteZickzack();
	}

	/**
	 * Mouse dragged zickzack.
	 * 
	 * @param evt
	 *            the evt
	 */
	private void mouseDraggedZickzack(MouseEvent evt) {
		int my = evt.getY();
		if (firstMove) {
			// at first move the old-my is set to the actual my
			omy = my;
			firstMove = false;
		}
		// the difference between old and new mouse coordinates
		int dy = my - omy;

		// the old coordintes of the element
		int oy = getY();

		setLocation(getX(), getDraggingY(oy, dy));

		y_rel = (double) getDraggingY(oy, dy) / aufzugschacht.getHeight();
	}

	/**
	 * Gets the dragging Y.
	 * 
	 * @param dy
	 *            the dy
	 * @param oy
	 *            the oy
	 * 
	 * @return the dragging Y
	 */
	private int getDraggingY(int oy, int dy) {
		int y;

		if (oy + dy >= 1 && oy + dy <= aufzugschacht.getHeight() - 1 - getHeight())
			y = oy + dy;
		else if (oy + dy < 1)
			y = 1;
		else
			y = aufzugschacht.getHeight() - 1 - getHeight();

		return y;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (imageIcon == null) {
			imageIcon = new ImageIcon(Helper.getFileURL(iconPath));
		}
		int imageWidth = imageIcon.getIconWidth();
		int imageHeight = imageIcon.getIconHeight();
		Image image = imageIcon.getImage();

		// stretch the outer images and concat so much images that it would fill the whole width
		// of the elevator
		if (getWidth() / imageWidth == 0) {
			g.drawImage(image, 0, 0, getWidth() % imageWidth, imageHeight, this);
		} else if (getWidth() / imageWidth == 1) {
			g.drawImage(image, 0, 0, imageWidth + (getWidth() % imageWidth), imageHeight, this);
		} else if (getWidth() / imageWidth == 2) {
			g.drawImage(image, 0, 0, imageWidth + ((getWidth() % imageWidth) / 2), imageHeight, this);
			g.drawImage(image, imageWidth + ((getWidth() % imageWidth) / 2), 0, imageWidth + ((getWidth() % imageWidth) / 2), imageHeight, this);
		} else {
			g.drawImage(image, 0, 0, imageWidth + ((getWidth() % imageWidth) / 2), imageHeight, this);
			int i = 0;
			for (i = imageWidth + ((getWidth() % imageWidth) / 2); i <= getWidth() - 32; i += imageWidth) {
				g.drawImage(image, i, 0, imageWidth, imageHeight, this);
			}
			g.drawImage(image, i, 0, imageWidth + ((getWidth() % imageWidth) / 2), imageHeight, this);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.Component#setLocation(int, int)
	 */
	@Override
	public void setLocation(int x, int y) {
		super.setLocation(x, y);
	}

	/**
	 * Sets the width of the zickzack
	 * 
	 * @param width
	 *            the with
	 */
	public void setWidth(int width) {
		super.setSize(width, getHeight());
	}

	/**
	 * Gets the relative Y position.
	 * 
	 * @return the y_rel
	 */
	public double getYRel() {
		firstMove = true;
		return y_rel;
	}
}
