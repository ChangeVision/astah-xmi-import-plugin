XMIインポートプラグイン
===============================

![dialog](https://raw.github.com/ChangeVision/astah-xmi-import-plugin/master/images/XMIインポート.png "XMIインポート")

最新バージョン
----------------
1.1.0

利用可能なastahバージョン
------------------
astah professional 6.7以上

(astah professional 6.6.4をご利用の方は、[バージョン1.0.0](http://astah.change-vision.com/plugins/xmi/1.0.0.html)をご利用ください)

概要
----------------
本プラグインはXMI形式のファイルからクラス図のモデル情報をインポートするプラグインです。

注意事項
----------------
 * 対応するXMIバージョンは2.1以降です。
 * クラス図のモデル情報のみインポートします。図や図要素などは移行できません。
 * 他ツールとの連携を行う場合は、一部の表記が異なる場合があります。

制限事項
----------------
 * 関連端の可視性の設定はサポートしません。
 * TemplateBinding,TemplateParameterはサポートしません。
 
インストール方法
----------------
0. [ここからjarをダウンロードします。](http://astah.change-vision.com/plugins/xmi/1.1.0.html)
1. astahのインストールフォルダ配下のpluginsフォルダに本プラグインのjarファイルを置きます。
2. astahを起動します。
3. メニューバーの「ツール」の中に「XMI」と表示されます。その下にある「XMIインポート」を実行してください。

使用例
----------------
1. astahを起動し、メニューバーの「ツール」の一番下に「XMIインポート」をクリックします。
2. ファイル選択ダイアログでxmiファイルを選択します。
3. 「インポート」というボタンをクリックするとインポートが始まります。
4. 終了すると「XMIファイルのインポートを終了しました。」というメッセージが表示されます。

アンインストール方法
------------------------
astahのインストールフォルダ配下のpluginsフォルダから本プラグインのjarファイルを削除してください。

ビルド & テスト
---------------
1. Astah Plug-in SDKをインストールする。
2. ソースコードをcloneする

    git clone [Repository URL]

3. ビルド

    astah-build

4. ユニットテスト

    astah-mvn test

拡張機構
-----------------
本プラグインは、対応していないモデルの種類のインポートを可能にする拡張機構を備えています。

拡張方法
-----------------
1. Astah Plug-in SDKをインストールする。
2. 本プラグインをローカルリポジトリに追加する。

    astah-mvn install:install-file -Dfile=xmi-1.0.0.jar -DgroupId=com.change_vision.xmi -DartifactId=xmi -Dversion=1.0.0 -Dpackaging=jar -DgeneratePom=true

3. 拡張用のプラグインを作成する。

    astah-generate-plugin

4. 拡張用のプラグインのpom.xmiに本プラグインを追加する。   

    <dependency>
        <groupId>com.change_vision.astah</groupId>
        <artifactId>xmi</artifactId>
        <version>1.0.0</version>
        <scope>provided</scope>
    </dependency>
    
5. Converterを実装する。

5.1 Usecase等モデルの場合は、ClassifierConverterを実装する。com.change_vision.astah.xmi.internal.convert.model以下のクラスを参考にしてください。

5.2 関連等のモデル間の関係の場合は、RelationConverterを実装する。com.change_vision.astah.xmi.internal.convert.relationship以下のクラスを参考にしてください。

6. BundleContextにConverterを登録する。BundleContextは、Activatorクラスのstart/stopメソッドの引数として渡されます。下記のActivatorのように登録してください。

	import org.osgi.framework.BundleActivator;
	import org.osgi.framework.BundleContext;
	
	import com.change_vision.astah.xmi.convert.model.ClassifierConverter;
	import com.change_vision.astah.xmi.convert.relationship.RelationshipConverter;
	
	public class Activator implements BundleActivator {
	
		public void start(BundleContext context) {
			//Classifierの場合
		    context.registerService(ClassifierConverter.class.getName(), new HogeConverter(), null);
		    //Relationshipの場合
		    context.registerService(RelationshipConverter.class.getName(), new FugaConverter(), null);
		}
	
		public void stop(BundleContext context) {
		}
		
	}

ライセンス
---------------
Copyright 2012 Change Vision, Inc.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this work except in compliance with the License.
You may obtain a copy of the License in the LICENSE file, or at:

   <http://www.apache.org/licenses/LICENSE-2.0>

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
