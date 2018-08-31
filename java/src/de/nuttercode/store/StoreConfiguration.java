package de.nuttercode.store;

import java.nio.file.Path;
import java.nio.file.Paths;

import de.nuttercode.util.Assurance;

/**
 * This class describes how a {@link de.nuttercode.store.Store} is configured. All settings
 * have default values except the {@link #storeName} and the {@link #basePath}.
 * Note: Once a {@link de.nuttercode.store.Store} has been created its proper functioning
 * depends on some of this attributes - choose appropriate values according to
 * the attributes' descriptions.
 * 
 * @author Johannes B. Latzel
 *
 */
public final class StoreConfiguration {

	/**
	 * suffix of the data files - should be a short acronym like "daf" or something
	 * similar. must not be empty or null.
	 */
	private String dataFileSuffix;

	/**
	 * suffix of the description files - should be a short acronym like "def" or
	 * something similar. must not be empty or null.
	 */
	private String descriptionFileSuffix;

	/**
	 * suffix of the id file - should be a short acronym like "id" or something
	 * similar. must not be empty or null.
	 */
	private String idFileSuffix;

	/**
	 * the size of {@link java.nio.ByteBuffer}s used throughout the library. must be
	 * positive.
	 */
	private int byteBufferSize;

	/**
	 * the minimum size of new data files. must be positive.
	 */
	private long minimumDataFileSize;

	/**
	 * the unique name of the {@link de.nuttercode.store.Store} within the {@link #basePath}
	 * directory. must not be empty or null.
	 */
	private final String storeName;

	/**
	 * the path to the directory in which all items related to the
	 * {@link de.nuttercode.store.Store} will be saved and loaded. must not be empty or null.
	 */
	private final Path basePath;

	public StoreConfiguration(StoreConfiguration configuration) {
		storeName = configuration.getStoreName();
		basePath = configuration.getBasePath();
		dataFileSuffix = configuration.getDataFileSuffix();
		descriptionFileSuffix = configuration.getDescriptionFileSuffix();
		byteBufferSize = configuration.getByteBufferSize();
		minimumDataFileSize = configuration.getMinimumDataFileSize();
		idFileSuffix = configuration.getIDFileSuffix();
	}

	/**
	 * default initializes all attributes other than {@link #storeName} and
	 * {@link #basePath}
	 * 
	 * @param storeName
	 *            the unique name of the {@link de.nuttercode.store.Store} within the
	 *            {@link #basePath}a
	 * @param basePath
	 *            the root directory path in which all store-files will be saved and
	 *            loaded
	 */
	public StoreConfiguration(String storeName, Path basePath) {
		Assurance.assureNotEmpty(storeName);
		Assurance.assureNotNull(basePath);
		this.storeName = storeName;
		this.basePath = basePath;
		dataFileSuffix = "daf";
		descriptionFileSuffix = "def";
		idFileSuffix = "id";
		byteBufferSize = 512;
		minimumDataFileSize = 1024;
	}

	public String getDataFileSuffix() {
		return dataFileSuffix;
	}

	public String getDescriptionFileSuffix() {
		return descriptionFileSuffix;
	}

	public int getByteBufferSize() {
		return byteBufferSize;
	}

	public long getMinimumDataFileSize() {
		return minimumDataFileSize;
	}

	public String getStoreName() {
		return storeName;
	}

	public Path getBasePath() {
		return basePath;
	}

	public void setDataFileSuffix(String dataFileSuffix) {
		Assurance.assureNotEmpty(dataFileSuffix);
		this.dataFileSuffix = dataFileSuffix;
	}

	public void setDescriptionFileSuffix(String descriptionFileSuffix) {
		Assurance.assureNotEmpty(dataFileSuffix);
		this.descriptionFileSuffix = descriptionFileSuffix;
	}

	public void setByteBufferSize(int byteBufferSize) {
		Assurance.assurePositive(byteBufferSize);
		this.byteBufferSize = byteBufferSize;
	}

	public void setMinimumDataFileSize(int minimumDataFileSize) {
		Assurance.assurePositive(minimumDataFileSize);
		this.minimumDataFileSize = minimumDataFileSize;
	}

	public void setIDFileSuffix(String idFileSuffix) {
		Assurance.assureNotEmpty(idFileSuffix);
		this.idFileSuffix = idFileSuffix;
	}

	/**
	 * @return {@link #storeName}
	 */
	public Path getStoreDirectory() {
		return Paths.get(getBasePath().toString(), getStoreName());
	}

	/**
	 * @return {@link #idFileSuffix}
	 */
	public String getIDFileSuffix() {
		return idFileSuffix;
	}

}
