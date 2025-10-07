{
  description = "sample-clojure-make-kanban";

  inputs = {
    nixpkgs.url = "github:NixOS/nixpkgs/nixos-unstable";
    flake-parts.url = "github:hercules-ci/flake-parts";
    treefmt-nix.url = "github:numtide/treefmt-nix";
  };

  outputs =
    inputs:
    inputs.flake-parts.lib.mkFlake { inherit inputs; } {
      systems = [
        "x86_64-linux"
        "aarch64-darwin"
      ];

      imports = [
        inputs.treefmt-nix.flakeModule
      ];

      perSystem =
        { pkgs, config, ... }:
        {
          packages.default = pkgs.hello;

          devShells.default = pkgs.mkShell {
            packages = with pkgs; [
              graalvm-ce
              (clojure.override { jdk = graalvm-ce; })
              clojure-lsp
              cljstyle
              nodejs_22
              pnpm_10
            ];
          };

          treefmt = {
            programs.nixfmt.enable = true;
            settings.formatter.cljstyle = {
              command = "${pkgs.cljstyle}/bin/cljstyle";
              options = [ "fix" ];
              includes = [
                "*.clj"
                "*.cljs"
                "*.cljc"
                "*.cljx"
                "*.edn"
              ];
            };
          };
        };
    };
}
