package de.fh.giessen.ringversuch.view;

import java.io.File;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.UIManager;

import org.apache.log4j.Logger;

import de.fh.giessen.ringversuch.common.Preferences;
import de.fh.giessen.ringversuch.controller.ControllerIn;
import de.fh.giessen.ringversuch.view.typesettings.ViewTypeSettings;

/**
 * <p>
 * Besides implementing {@link SwingViewManager} functionality, this class takes
 * care of dropping incoming calls to AWT event thread and also escaping
 * outgoing ones from it. AWT event thread issues are centrally handled here, so
 * there is no need to take care of these anywhere else.
 * </p>
 * 
 * @threadSave
 * @author Alexander Kerner
 * @lastVisit 2009-08-26
 * 
 */
public class SwingViewManagerImpl implements SwingViewManager {

	private final static Logger LOGGER = Logger
			.getLogger(SwingViewManagerImpl.class);
	private final Map<ViewType, SwingView> map = new ConcurrentHashMap<ViewType, SwingView>();
	private volatile ViewTypeSettings settings;
	private final ControllerIn controller;

	public SwingViewManagerImpl(ControllerIn controller) {
		this.controller = controller;
		setLookAndFeel();
	}

	@Override
	public void addView(ViewType type, SwingView view) {
		map.put(type, view);
	}

	@Override
	public void hideView() {
		throw new IllegalStateException();
	}

	@Override
	public void showView() {
		ViewUtils.dropToEventThread(new Runnable() {
			public void run() {
				map.get(ViewType.MAIN).showView();
			}
		});
	}
	
	@Override
	public void destroyView() {
		ViewUtils.dropToEventThread(new Runnable() {
			public void run() {
		for(SwingView v : map.values()){
			v.destroyView();
		}
			}
		});
	}

	@Override
	public void switchView(final ViewState state) {
		ViewUtils.dropToEventThread(new Runnable() {
			public void run() {
				switch (state) {
				case NORMAL:
					map.get(ViewType.SETTINGS).hideView();
					break;
				case SETTINGS_ACTIVE:
					map.get(ViewType.SETTINGS).showView();
					break;
				default:
					throw new IllegalStateException("view state " + state
							+ " unknown");
				}
			}
		});
	}

	@Override
	public ViewTypeSettings outgoingSetSettings() {
		return get();
	}

	@Override
	public void outgoingPrintMessage(final String message, final boolean isError) {
		ViewUtils.dropToEventThread(new Runnable() {
			public void run() {
				map.get(ViewType.MAIN).outgoingPrintMessage(message, isError);
			}
		});
	}

	@Override
	public void outgoingSetOnline() {
		ViewUtils.dropToEventThread(new Runnable() {
			public void run() {
				map.get(ViewType.MAIN).outgoingSetOnline();
			}
		});
	}

	@Override
	public void outgoingSetProgress(final int current, final int max) {
		ViewUtils.dropToEventThread(new Runnable() {
			public void run() {
				map.get(ViewType.MAIN).outgoingSetProgress(current, max);
			}
		});
	}

	@Override
	public void outgoingSetReady() {
		ViewUtils.dropToEventThread(new Runnable() {
			public void run() {
				map.get(ViewType.MAIN).outgoingSetReady();
			}
		});
	}

	@Override
	public void outgoingSetSettings(final ViewTypeSettings settings) {
		set(settings);
		ViewUtils.dropToEventThread(new Runnable() {
			public void run() {
				map.get(ViewType.SETTINGS).outgoingSetSettings(settings);
			}
		});
	}
	
	@Override
	public void outgoingSetSelectedFiles(final File[] files) {
		ViewUtils.dropToEventThread(new Runnable() {
			public void run() {
				map.get(ViewType.MAIN).outgoingSetSelectedFiles(files);
			}
		});
	}

	@Override
	public void outgoingSetWorking() {
		ViewUtils.dropToEventThread(new Runnable() {
			public void run() {
				map.get(ViewType.MAIN).outgoingSetWorking();
			}
		});
	}

	@Override
	public void outgoingShowError(final String message) {
		ViewUtils.dropToEventThread(new Runnable() {
			public void run() {
				map.get(ViewType.MAIN).outgoingShowError(message);
			}
		});
	}

	@Override
	public void incomingCancel() {
		ViewUtils.escapeFromEventThread(new Runnable(){
			@Override
			public void run() {
				controller.incomingCancel();
			}
		});
	}

	@Override
	public void incomingDetect() {
		ViewUtils.escapeFromEventThread(new Runnable(){
			@Override
			public void run() {
				controller.incomingDetect();
				
			}
		});
	}

	@Override
	public void incomingLoadSettings(final File file) {
		ViewUtils.escapeFromEventThread(new Runnable(){
				@Override
				public void run() {
					controller.incomingLoadSettings(file);
				}
			});
			
	}

	@Override
	public void incomingSaveSettings(final ViewTypeSettings settings) {
		ViewUtils.escapeFromEventThread(new Runnable(){
				@Override
				public void run() {
					controller.incomingSaveSettings(settings);
				}
			});	
	}

	@Override
	public void incomingSetOutDir(final File selectedFile) {
		ViewUtils.escapeFromEventThread(new Runnable(){
			@Override
			public void run() {
				controller.incomingSetOutDir(selectedFile);
			}
		});
	}

	@Override
	public void incomingSetSelectedFiles(final File[] inputFiles) {
		ViewUtils.escapeFromEventThread(new Runnable(){
				@Override
				public void run() {
					controller.incomingSetSelectedFiles(inputFiles);
				}
			});
	}

	@Override
	public void incomingSetSettings(final ViewTypeSettings settings) {
		
			ViewUtils.escapeFromEventThread(new Runnable(){
				@Override
				public void run() {
					controller.incomingSetSettings(settings);
				}
			});
		
	}

	@Override
	public void incomingStart() {
		ViewUtils.escapeFromEventThread(new Runnable(){
			@Override
			public void run() {
				controller.incomingStart();
			}
		});
	}

	private static void setLookAndFeel() {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				if (Preferences.NATIVE_LAF) {
					try {
						UIManager.setLookAndFeel(UIManager
								.getSystemLookAndFeelClassName());
					} catch (Exception e) {
						LOGGER.warn("could not init native look and feel", e);
					}
				}
			}
		});
	}

	@Override
	public ViewTypeSettings get() {
		return settings;
	}

	@Override
	public void set(ViewTypeSettings settings) {
		this.settings = settings;
	}

	@Override
	public void incomingShutdown() {		
		ViewUtils.escapeFromEventThread(new Runnable(){
			@Override
			public void run() {
				controller.incomingShutdown();
			}
		});
	}

	@Override
	public void outgoingShutdown() {
		ViewUtils.escapeFromEventThread(new Runnable(){
			@Override
			public void run() {
				LOGGER.info("shutting down Application GUI");
				for(SwingView v : map.values()){
					v.hideView();
				}
				destroyView();
				ViewUtils.shutdown();
				LOGGER.info("Application GUI dead");
			}
		});		
	}

	
}
