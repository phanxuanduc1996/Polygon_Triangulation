package polygon;

import javax.swing.JApplet;

public class PolygonTriangulation extends JApplet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private PolygonDraw polygonDraw;

	public void init() {
		try {
			java.awt.EventQueue.invokeAndWait(new Runnable() {
				public void run() {
					initComponents();
				}
			});
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		setSize(732, 360);

		polygonDraw = new PolygonDraw(580, 360);
		polygonDraw.setVisible(true);
		getContentPane().add(polygonDraw);

	}

	private void initComponents() {

		buttonGroup1 = new javax.swing.ButtonGroup();
		jPanel1 = new javax.swing.JPanel();
		KongButton = new javax.swing.JButton();
		ResetPolygonButton = new javax.swing.JButton();
		jLabel1 = new javax.swing.JLabel();
		CalcAutomaticRadioBut = new javax.swing.JRadioButton();
		jLabel2 = new javax.swing.JLabel();
		PauseTimeField = new javax.swing.JTextField();
		CalcDirectlyRadioBut = new javax.swing.JRadioButton();

		jPanel1.setBackground(new java.awt.Color(255, 255, 255));
		jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

		KongButton.setFont(new java.awt.Font("Tahoma", 0, 10));
		KongButton.setText("Calculate");
		KongButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				KongButtonActionPerformed(evt);
			}
		});

		ResetPolygonButton.setFont(new java.awt.Font("Tahoma", 0, 10));
		ResetPolygonButton.setText("Reset Polygon");
		ResetPolygonButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				ResetPolygonButtonActionPerformed(evt);
			}
		});

		jLabel1.setFont(new java.awt.Font("Tahoma", 1, 11));
		jLabel1.setText("Polygon Triangulation");

		buttonGroup1.add(CalcAutomaticRadioBut);
		CalcAutomaticRadioBut.setText("Step by Step");

		jLabel2.setFont(new java.awt.Font("Tahoma", 1, 11));
		jLabel2.setText("Pause Time(ms)");

		PauseTimeField.setText("2500");

		buttonGroup1.add(CalcDirectlyRadioBut);
		CalcDirectlyRadioBut.setSelected(true);
		CalcDirectlyRadioBut.setText("Triangulating");

		javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
		jPanel1.setLayout(jPanel1Layout);
		jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(jPanel1Layout.createSequentialGroup().addContainerGap().addGroup(jPanel1Layout
						.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel1)
						.addComponent(ResetPolygonButton)
						.addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
								.addComponent(CalcDirectlyRadioBut, javax.swing.GroupLayout.Alignment.LEADING,
										javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
										Short.MAX_VALUE)
								.addComponent(CalcAutomaticRadioBut, javax.swing.GroupLayout.Alignment.LEADING,
										javax.swing.GroupLayout.DEFAULT_SIZE, 84, Short.MAX_VALUE)))
						.addContainerGap(10, Short.MAX_VALUE))
				.addGroup(javax.swing.GroupLayout.Alignment.TRAILING,
						jPanel1Layout.createSequentialGroup().addContainerGap(24, Short.MAX_VALUE)
								.addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
										.addComponent(PauseTimeField, javax.swing.GroupLayout.PREFERRED_SIZE, 50,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addComponent(jLabel2))
								.addContainerGap())
				.addGroup(jPanel1Layout
						.createSequentialGroup().addContainerGap().addComponent(KongButton,
								javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
						.addContainerGap(18, Short.MAX_VALUE)));
		jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(jPanel1Layout.createSequentialGroup().addGap(19, 19, 19).addComponent(jLabel1)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
						.addComponent(CalcDirectlyRadioBut).addGap(18, 18, 18).addComponent(CalcAutomaticRadioBut)
						.addGap(11, 11, 11).addComponent(jLabel2)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(PauseTimeField, javax.swing.GroupLayout.PREFERRED_SIZE, 21,
								javax.swing.GroupLayout.PREFERRED_SIZE)
						.addGap(18, 18, 18).addComponent(KongButton).addGap(50, 50, 50).addComponent(ResetPolygonButton)
						.addContainerGap(106, Short.MAX_VALUE)));

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
				javax.swing.GroupLayout.Alignment.TRAILING,
				layout.createSequentialGroup().addContainerGap(476, Short.MAX_VALUE).addComponent(jPanel1,
						javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
						javax.swing.GroupLayout.PREFERRED_SIZE)));
		layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(
				jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
	}// </editor-fold>//GEN-END:initComponents

	private void ResetPolygonButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_ResetPolygonButtonActionPerformed
		polygonDraw.ResetPolygon();
	}// GEN-LAST:event_ResetPolygonButtonActionPerformed

	private void KongButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_KongButtonActionPerformed

		// directly
		if (CalcDirectlyRadioBut.isSelected()) {
			polygonDraw.ApplyKongAlgo(false, 0);
		}
		// automatic
		else if (CalcAutomaticRadioBut.isSelected()) {
			int pausetime;
			try {
				pausetime = Integer.parseInt(PauseTimeField.getText());
			} catch (NumberFormatException ex) {
				pausetime = 2000;
			}

			polygonDraw.ApplyKongAlgo(true, pausetime);
		}

	}// GEN-LAST:event_KongButtonActionPerformed

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JRadioButton CalcAutomaticRadioBut;
	private javax.swing.JRadioButton CalcDirectlyRadioBut;
	private javax.swing.JButton KongButton;
	private javax.swing.JTextField PauseTimeField;
	private javax.swing.JButton ResetPolygonButton;
	private javax.swing.ButtonGroup buttonGroup1;
	private javax.swing.JLabel jLabel1;
	private javax.swing.JLabel jLabel2;
	private javax.swing.JPanel jPanel1;
	// End of variables declaration//GEN-END:variables

}
