package blocks.demo.properties;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

import rx.functions.Func1;
import rx.observables.SwingObservable;
import rx.observers.EmptyObserver;
import rx.schedulers.SwingScheduler;
import blocks.core.Property;

public class DemoFrame extends ApplicationFrame {

	public static final String PROP_SEARCH = "search";
	private Property<String> search;

	public static final String PROP_RESULTS = "results";
	private Property<List<String>> results;

	public DemoFrame() {
		super("DEMO");

		search = new Property<>(PROP_SEARCH);
		results = new Property<>(PROP_RESULTS);

		// Création de la fenêtre

		JLabel searchLabel = new JLabel("Search :");
		final JTextField textfield = new JTextField();
		JButton searchButton = new JButton("GO");

		JPanel header = new JPanel(new BorderLayout());
		header.add(searchLabel, BorderLayout.NORTH);
		header.add(textfield, BorderLayout.CENTER);
		header.add(searchButton, BorderLayout.EAST);

		JList<String> resultList = new JList<>();
		final DefaultListModel<String> listModel = new DefaultListModel<>();
		listModel.addElement("test");
		listModel.addElement("test");
		listModel.addElement("test");
		resultList.setModel(listModel);

		add(header, BorderLayout.NORTH);
		add(resultList, BorderLayout.CENTER);

		// Bind des properties

		search.listen(
				SwingScheduler.getInstance(),
				SwingObservable.fromButtonAction(searchButton).map(
						new Func1<ActionEvent, String>() {

							@Override
							public String call(ActionEvent event) {
								return textfield.getText();
							}

						}));

		results.sendTo(SwingScheduler.getInstance(),
				new EmptyObserver<List<String>>() {
					@Override
					public void onNext(List<String> values) {
						listModel.removeAllElements();
						for (String value : values) {
							listModel.addElement(value);
						}
					}
				});

		pack();
		setSize(400, 500);
		RefineryUtilities.centerFrameOnScreen(this);
	}

	public Property<String> searchProperty() {
		return search;
	}

	public Property<List<String>> resultsProperty() {
		return results;
	}

	/**
	 * UID.
	 */
	private static final long serialVersionUID = 5511255292137552252L;

}
