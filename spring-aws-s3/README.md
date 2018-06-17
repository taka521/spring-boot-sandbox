# AWS S3のファイルアップロード・ダウンロード

## 概要

AWS S3 に対して、

* ファイルのアップロード
* ファイルのダウンロード

を行うサンプルコード。  
なお、ファイルは暗号化&圧縮する。

暗号化アルゴリズムは AES、圧縮は GZIP とする。

## 参考にしたサイト

* [AES暗号アルゴリズムを使用してデータを暗号化する](https://techbooster.org/android/application/6629/)
* [暗号化してから圧縮するか、圧縮してから暗号化するか](http://syo.cocolog-nifty.com/freely/2009/02/post-21a7.html)
* [暗号技術入門04 ブロック暗号のモード〜ブロック暗号をどのように繰り返すのか〜](http://www.spiritek.co.jp/spkblog/2016/12/01/%E6%9A%97%E5%8F%B7%E6%8A%80%E8%A1%93%E5%85%A5%E9%96%8004-%E3%83%96%E3%83%AD%E3%83%83%E3%82%AF%E6%9A%97%E5%8F%B7%E3%81%AE%E3%83%A2%E3%83%BC%E3%83%89%E3%80%9C%E3%83%96%E3%83%AD%E3%83%83%E3%82%AF/)
* [S3にjavaでアップロードとダウンロードメモ](https://qiita.com/hokke/items/e3650decbc57cae8cc42)

## 暗号化アルゴリズムについて

今回は暗号化アルゴリズムに `AES` を用いている。  
AESはブロック暗号の一種であるため、ある一定のデータ数を「ブロック」として扱い、暗号化する。  

AESの鍵は 128, 192, 256bit の何れかで、Javaの場合は 128bit がデフォルト。  
なお、1つのブロック長は 128bit(16byte) である。

### ブロック暗号

ブロック暗号にも

* ECBモード: Electronic CodeBook mode(電子符号表モード)
* CBCモード: Cipher Book Chaining mode(暗号ブロック連鎖モード)

の２種類が存在する。  
なお、ECBモードは単に平文ブロックを暗号化したものが、暗号化ブロックとなる。  
単純な暗号化であるため、使用は推奨されない。

CBCモードは、1つ前の暗号ブロックと平文ブロックの排他的論理和(XOR)を取ってから暗号化を行う。  
しかし、最初の平文ブロックは１つ前の暗号ブロックが無い。  
そのため、最初に使用する暗号ブロックの代わりに「初期ベクトル(Initialization Vector)」というものを使用する。  
=> つまり、IVもブロックサイズと同じデータ量にしなければいけない。

ブロック暗号は、暗号化対象のデータを特定サイズに分割していくが、綺麗に分割出来ない場合はパディングで穴埋めされる。  
パディング方式は

* NoPadding
  * パディングなし
  * 平文データが、必ずブロック長で割り切れるのであれば指定してよい。
* PKCS5Padding
  * 平文ブロックがブロック長に満たない場合、ブロック長になるようにパディング埋めする。
  * パディングは 8byte(64bit) まで行う。
  * Javaではこの PKCS5Padding をデフォルトサポートしている。（が、実際には PKCS7Padding らしい）
  * [Java (JCE) で AES 暗号化するときの PKCS#5 の実態は PKCS#7 なのか？](http://d.hatena.ne.jp/naga_sawa/20170730/1501404099)
* PKCS7Padding
  * 基本的にはPKCS5Paddingと同じ。
  * パディングしてくれるのは 16byte(128bit) までである。
* ISO10126-2
* ISO9797