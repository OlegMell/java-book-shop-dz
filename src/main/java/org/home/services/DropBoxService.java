package org.home.services;

import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.*;
import com.dropbox.core.v2.sharing.ListFilesResult;
import com.dropbox.core.v2.users.FullAccount;
import org.springframework.data.mongodb.core.query.Meta;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class DropBoxService {
    private static final String ACCESS_TOKEN = "sl.ApR_aRy50brq6C1rTwueB7bHlnqA_jupvdXo5Z13hjf-9_R3NbC7zElChG463O-p5RfF-110fcrkjymboByn5FnGYadHQi--pL0WFAriv274k7FMKpZcgUuLn1o4BtjuCx5ciWkrV1E";

    private final DbxClientV2 client;

    public DropBoxService() throws DbxException {
        DbxRequestConfig config = DbxRequestConfig.newBuilder("dropbox/book_shop_java").build();
        this.client = new DbxClientV2(config, ACCESS_TOKEN);
        FullAccount account = client.users().getCurrentAccount();
    }

    public String uploadFile(InputStream in, String fileName) {
        try {
            FileMetadata metadata = this.client.files().uploadBuilder("/book_shop_java/" + fileName)
                    .uploadAndFinish(in);
            return this.client.files().getTemporaryLink(metadata.getPathLower()).getLink();
        } catch (IOException | DbxException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ListFolderResult getFiles() throws DbxException {
        var result = this.client.files().listFolder("/book_shop_java");
        List<Metadata> m = new ArrayList<>();
        while (true) {
            for (Metadata metadata : result.getEntries()) {
                m.add(metadata);
            }

            if (!result.getHasMore()) {
                break;
            }

            result = this.client.files().listFolderContinue(result.getCursor());
        }

        List<FileMetadata> fm = new ArrayList<>();
        m.forEach(item -> {
            try {
                fm.add(this.downloadImages(item.getPathLower(), item.getName()));
            } catch (DbxException | IOException e) {
                e.printStackTrace();
            }
        });

        return null;
    }

    public FileMetadata downloadImages (String path, String name) throws DbxException, IOException {
        FileOutputStream ot = new FileOutputStream(name);
        var fs = this.client.files().download(path).download(ot);
        int a = 0;
        return fs;
    }

}
