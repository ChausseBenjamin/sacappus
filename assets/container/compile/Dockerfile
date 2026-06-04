FROM archlinux:latest AS sacappus-compiler

# Keep layers small and ordered by stability.
# Most-likely-to-change packages (TexLive) are at the bottom so they don't
# invalidate cached layers above them.  Cache is cleaned once at the end.

RUN pacman -Syu --noconfirm

# ── Toolchain ────────────────────────────────────────────────────────────────
RUN pacman -S --noconfirm --needed base-devel

# ── Utilities (rarely change) ────────────────────────────────────────────────
RUN pacman -S --noconfirm --needed which
RUN pacman -S --noconfirm --needed findutils
RUN pacman -S --noconfirm --needed libuv

# ── Language runtimes ────────────────────────────────────────────────────────
RUN pacman -S --noconfirm --needed r
RUN pacman -S --noconfirm --needed python
RUN pacman -S --noconfirm --needed uv

# ── Build tools ──────────────────────────────────────────────────────────────
RUN pacman -S --noconfirm --needed entr
RUN pacman -S --noconfirm --needed pandoc

# ── TeX Live (most likely to need additions — kept at end) ──────────────────
RUN pacman -S --noconfirm --needed texlive-core
RUN pacman -S --noconfirm --needed texlive-xetex
RUN pacman -S --noconfirm --needed texlive-latexextra
RUN pacman -S --noconfirm --needed texlive-binextra
RUN pacman -S --noconfirm --needed texlive-fontsextra
RUN pacman -S --noconfirm --needed texlive-langextra
RUN pacman -S --noconfirm --needed texlive-langfrench
RUN pacman -S --noconfirm --needed texlive-mathscience
RUN pacman -S --noconfirm --needed texlive-bibtexextra
RUN pacman -S --noconfirm --needed biber

# ── Clean pacman cache ───────────────────────────────────────────────────────
RUN pacman -Scc --noconfirm

# ── R packages (knitr ecosystem) ─────────────────────────────────────────────
RUN R -e "install.packages(c('knitr', 'rmarkdown', 'reticulate'), \
  repos='https://cloud.r-project.org/')" \
  && rm -rf /tmp/Rtmp*

# ── Runtime tools (git for .gitignore-based file watching, rsync for staging) ─
RUN pacman -S --noconfirm --needed git
RUN pacman -S --noconfirm --needed rsync

# Bake a .git repo at /data so `git check-ignore --no-index` works whenever a
# project mounts its content at /data/in/… — git finds the .git at /data.
RUN mkdir -p /data && git init -q /data && git -C /data config user.email compiler@sacappus && git -C /data config user.name compiler

# ── Create non‑root user so compiled output isn't owned by root ─────────────
RUN useradd -m -u 1000 builder

# ── Compiler entrypoint ──────────────────────────────────────────────────────
COPY compiler /usr/local/bin/compiler
RUN chmod +x /usr/local/bin/compiler

# Biber is installed to /usr/bin/vendor_perl/ (Arch perl policy) which is not
# in the default PATH — symlink it so the compiler can find it.
RUN ln -sf /usr/bin/vendor_perl/biber /usr/local/bin/biber

WORKDIR /data

ENTRYPOINT ["/usr/local/bin/compiler"]
