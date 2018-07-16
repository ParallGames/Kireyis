# create build directories
echo -e "\n\033[1;31mCreating directories\033[0m";

	# remove old build
if [ -d "build" ];then
	rm -r build
fi

mkdir build

mkdir build/client
mkdir build/client/class

mkdir build/server
mkdir build/server/class

mkdir build/release

# compilation
echo -e "\n\033[1;31mCompiling client\033[0m";
javac -sourcepath src src/kireyis/client/Main.java -d build/client/class -version

echo -e "\n\033[1;31mCompiling server\033[0m";
javac -sourcepath src src/kireyis/server/Main.java -d build/server/class -version

# copy resources
echo -e "\n\033[1;31mCopying resources\033[0m";
cp -r src/resources build/client/class/resources

# archive
echo -e "\n\033[1;31mArchiving\033[0m";
jar cmf manifests/client/MANIFEST.MF build/release/Kireyis.jar -C build/client/class .
jar cmf manifests/server/MANIFEST.MF build/release/KireyisServer.jar -C build/server/class .

# end
echo -e "\n\033[1;31mCompleted\033[0m\n";
