package ac.kr.kaist.kyoungrok.hadoop_pagerank_new.util;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.FileUtil;
import org.apache.hadoop.fs.Path;

public class PathHelper {
	private enum PathName {
		NONE, PARSE, METANODES, TITLEIDMAP, IDTITLEMAP
	}

	public static final String NAME_PARSE = "parse";
	public static final String NAME_META_NODE = "metanodes";
	public static final String NAME_TITLE_ID_MAP = "titleidmap";
	public static final String NAME_ID_TITLE_MAP = "idtitlemap";
	
	private static final Path emptyPath = new Path(" ");

	public static Path getPathForName(String pathName, Configuration conf) {	
		Path basePath = new Path(conf.get("output_path"));
		PathName pName = PathName.NONE;

		try {
			pName = PathName.valueOf(pathName.toUpperCase());
		} catch (IllegalArgumentException e) {
			System.err.println(e.getMessage());
			return emptyPath;
		}

		switch (pName) {
		case PARSE:
			return new Path(basePath, new Path(NAME_PARSE));
		case METANODES:
			return new Path(new Path(basePath, new Path(NAME_PARSE)), new Path(
					NAME_META_NODE));
		case TITLEIDMAP:
			return new Path(new Path(basePath, new Path(NAME_PARSE)), new Path(
					NAME_TITLE_ID_MAP));
		case IDTITLEMAP:
			return new Path(new Path(basePath, new Path(NAME_PARSE)), new Path(
					NAME_ID_TITLE_MAP));
		default:
			return emptyPath;
		}
	}

	public static FileSystem getFileSystem(Path path, Configuration conf)
			throws IOException {
		return FileSystem.get(path.toUri(), conf);
	}

	public static Path[] listDir(Path dirPath, Configuration conf)
			throws IOException {
		FileSystem fs = FileSystem.get(dirPath.toUri(), conf);
		return FileUtil.stat2Paths(fs.listStatus(dirPath));
	}

	public static Path[] getCacheFiles(String cacheName, Configuration conf)
			throws IOException {
		PathName name = PathName.NONE;

		try {
			name = PathName.valueOf(cacheName.toUpperCase());
		} catch (IllegalArgumentException e) {
			System.err.println(e.getMessage());
			return new Path[0];
		}

		Path cachePath = new Path(" ");
		switch (name) {
		case TITLEIDMAP:
			cachePath = getPathForName(NAME_TITLE_ID_MAP, conf);
			return listDir(cachePath, conf);

		default:
			break;
		}

		return new Path[0];
	}
}
