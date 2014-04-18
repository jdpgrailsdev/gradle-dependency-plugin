package org.gradle.plugins.dependency.update

import com.lostcreations.mojos.version.Version

/**
 * Convenience class used to compare dependency versions.
 *
 * @author Joanthan Pearlin
 */
class DependencyVersion {

	/**
	 * The parsed version string as an object.
	 */
	private Version version

	/**
	 * Constructs a new dependency version from the provided version string.
	 * @param version The version of the dependency as a string.
	 */
	DependencyVersion(String version) {
		this.version = Version.parse(version)
	}

	/**
	 * Determines if this dependency version is newer than the provided dependency version.  The
	 * caller can control whether or not the major version is inspected as part of the comparison.
	 * If the major version is included, it is checked first before comparing the remaining parts
	 * of the version number.  If it is not included, the comparison will return a result only
	 * if the current major numbers are equivalent.  Otherwise, the versions will be considered
	 * equivalent if the major version numbers are equal, but major releases are not included
	 * in the comparison.
	 * @param other The other dependency version to be compared to this.
	 * @param includeMajorReleases Whether or not to include the comparison of major releases.
	 * @return {@code true} if this dependency version is newer than the provided dependency version
	 * 	or {@code false} if it is not newer.
	 */
	boolean isNewerThan(DependencyVersion other, boolean includeMajorReleases) {
		boolean isNewer = false

		if(includeMajorReleases) {
			isNewer = other ? version.getMajorAsInt().compareTo(other.version.getMajorAsInt()) > 0 : false
		}

		if(!isNewer) {
			isNewer =  (compareTo(other) > 0)
		}

		isNewer
	}

	private int compareToMajorVersion(DependencyVersion other) {
		other ? version.getMajorAsInt().compareTo(other.version.getMajorAsInt()) : 1
	}

	@Override
	public int compareTo(DependencyVersion other) {
		int comparison = 1

		if(other) {
			Version otherVersion = other.version
			if(other.version.getMajorAsInt() == version.getMajorAsInt()) {
				comparison = version.getMinorAsInt().compareTo(otherVersion.getMinorAsInt())
				if(comparison == 0) {
					comparison = version.getRevisionAsInt().compareTo(otherVersion.getRevisionAsInt())
					if(comparison == 0) {
						comparison = version.getSuffix()?.compareTo(otherVersion.getSuffix()) ?: 1
					}
				}
			}
		}

		comparison
	}
}