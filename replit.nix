{ pkgs }: {
    deps = [
		pkgs.gh
  pkgs.scc
  pkgs.nodePackages.prettier
        pkgs.neovim
        pkgs.fish
        pkgs.graalvm17-ce
        pkgs.maven
        pkgs.replitPackages.jdt-language-server
        pkgs.replitPackages.java-debug
    ];
}