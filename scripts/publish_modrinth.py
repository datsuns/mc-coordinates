#!/usr/bin/env python3
"""
Modrinth Publish Script for Simple Coordinates (mc-coordinates)
Publishes Fabric & NeoForge builds to Modrinth via clean REST API requests.
"""

import os
import sys
import json
import argparse
import urllib.parse
from pathlib import Path

if hasattr(sys.stdout, "reconfigure"):
    sys.stdout.reconfigure(encoding="utf-8", errors="replace")
if hasattr(sys.stderr, "reconfigure"):
    sys.stderr.reconfigure(encoding="utf-8", errors="replace")

try:
    import requests
except ImportError:
    requests = None


def read_gradle_properties(root_dir: Path) -> dict:
    props = {}
    props_path = root_dir / "gradle.properties"
    if props_path.exists():
        for line in props_path.read_text(encoding="utf-8").splitlines():
            line = line.strip()
            if line and not line.startswith("#") and "=" in line:
                k, v = line.split("=", 1)
                props[k.strip()] = v.strip()
    return props


def build_release_changelog_title(loader_type: str, mod_version: str, mc_version: str) -> str:
    loader_name = "Fabric" if loader_type == "fabric" else "NeoForge"
    return f"# v{mod_version} for {loader_name} Minecraft {mc_version}"


def get_default_release_changelog(root_dir: Path, mod_version: str, mc_version: str) -> str:
    changelog_path = root_dir / "release_changelog.md"
    if changelog_path.exists():
        return changelog_path.read_text(encoding="utf-8")
    return (
        f"# Simple Coordinates v{mod_version}\n\n"
        f"Release for Minecraft {mc_version} (Fabric & NeoForge)!\n\n"
        "### Features\n"
        "- Displays player coordinates, direction, and camera angle on HUD.\n"
    )


def resolve_project_base62_id(project_id_or_slug: str, token: str) -> str:
    if not project_id_or_slug or not requests:
        return project_id_or_slug
    url = f"https://api.modrinth.com/v2/project/{urllib.parse.quote(project_id_or_slug)}"
    headers = {"User-Agent": f"datsuns/{project_id_or_slug} (me.datsuns@gmail.com)"}
    if token:
        headers["Authorization"] = token
    try:
        response = requests.get(url, headers=headers, timeout=10)
        if response.status_code in (200, 201):
            data = response.json()
            base62_id = data.get("id")
            if base62_id:
                if project_id_or_slug != base62_id:
                    print(f"[Info] Resolved project slug '{project_id_or_slug}' -> Modrinth Base62 ID: {base62_id}")
                return base62_id
        else:
            print(f"[Warning] Could not fetch project info from Modrinth (HTTP {response.status_code}). Using '{project_id_or_slug}' directly.")
    except Exception as e:
        print(f"[Warning] Exception while resolving project ID: {e}. Using '{project_id_or_slug}' directly.")
    return project_id_or_slug


def main():
    parser = argparse.ArgumentParser(description="Publish Simple Coordinates to Modrinth.")
    parser.add_argument("--token", help="Modrinth API Token (defaults to MODRINTH_TOKEN env var)")
    parser.add_argument("--project-id", help="Modrinth project slug or ID (defaults to 'modrinth_project_id' from gradle.properties)")
    parser.add_argument("--version", help="Mod version (defaults to mod_version from gradle.properties)")
    parser.add_argument("--minecraft-version", help="Minecraft version (defaults to minecraft_version from gradle.properties)")
    parser.add_argument("--changelog", help="Path to changelog file or changelog text string")
    parser.add_argument("--release-type", choices=["release", "beta", "alpha"], default="release", help="Version release type")
    parser.add_argument("--dry-run", action="store_true", help="Print payload and files without executing API request")
    parser.add_argument("--loader", choices=["fabric", "neoforge", "both", "all"], default="all", help="Which loader JARs to publish (fabric, neoforge, or all/both separately)")
    args = parser.parse_args()

    root_dir = Path(__file__).resolve().parent.parent
    props = read_gradle_properties(root_dir)

    token = args.token or os.environ.get("MODRINTH_TOKEN", "").strip()
    raw_project_id = args.project_id or props.get("modrinth_project_id", "OT4jnFfV")
    project_id = resolve_project_base62_id(raw_project_id, token) if (token or not args.dry_run) else raw_project_id
    mod_version = args.version or props.get("mod_version", "1.4.0")
    mc_version = args.minecraft_version or props.get("minecraft_version", "26.3")
    archives_base_name = props.get("archives_base_name", "simple-coordinates")

    if args.changelog:
        changelog_file = Path(args.changelog)
        if changelog_file.exists():
            release_changelog_text = changelog_file.read_text(encoding="utf-8")
        else:
            release_changelog_text = args.changelog
    else:
        release_changelog_text = get_default_release_changelog(root_dir, mod_version, mc_version)

    fabric_jar = root_dir / "fabric" / "build" / "libs" / f"{archives_base_name}-{mod_version}-fabric-mc{mc_version}.jar"
    neoforge_jar = root_dir / "neoforge" / "build" / "libs" / f"{archives_base_name}-{mod_version}-neoforge-mc{mc_version}.jar"

    missing_jars = []
    if args.loader in ("fabric", "both", "all") and not fabric_jar.exists():
        missing_jars.append(str(fabric_jar))
    if args.loader in ("neoforge", "both", "all") and not neoforge_jar.exists():
        missing_jars.append(str(neoforge_jar))

    if missing_jars:
        print(f"[Warning] The following built JAR files were not found:\n" + "\n".join(f"  - {j}" for j in missing_jars))
        print("Please run `./gradlew build` before publishing.")
        if not args.dry_run:
            sys.exit(1)

    is_dry_run = args.dry_run or not token
    if not token and not args.dry_run:
        print("[Info] No MODRINTH_TOKEN provided. Automatically running in --dry-run mode.\n")

    loaders_to_publish = ["fabric", "neoforge"] if args.loader in ("both", "all") else [args.loader]
    for idx, ldr in enumerate(loaders_to_publish):
        if idx > 0:
            print("\n" + "=" * 60 + "\n")
        publish_single_loader(ldr, project_id, mod_version, mc_version, release_changelog_text, args.release_type, token, is_dry_run, root_dir, fabric_jar, neoforge_jar)


def publish_single_loader(loader_type: str, project_id: str, mod_version: str, mc_version: str, release_changelog_text: str, release_type: str, token: str, is_dry_run: bool, root_dir: Path, fabric_jar: Path, neoforge_jar: Path):
    loader_name = "Fabric" if loader_type == "fabric" else "NeoForge"
    if loader_type == "fabric":
        jar_file = fabric_jar
        file_key = "fabric_jar"
        name = f"Simple Coordinates v{mod_version} - Fabric (MC {mc_version})"
        version_number = f"v{mod_version}+{loader_type}-mc{mc_version}"
        loaders = ["fabric"]
        dependencies = [
            {"project_id": "P7dR8mSH", "dependency_type": "required"},  # Fabric API
            {"project_id": "9s6osm5g", "dependency_type": "required"},  # Cloth Config
            {"project_id": "mOgUt4GM", "dependency_type": "required"},  # Mod Menu
        ]
    else:
        jar_file = neoforge_jar
        file_key = "neoforge_jar"
        name = f"Simple Coordinates v{mod_version} - NeoForge (MC {mc_version})"
        version_number = f"v{mod_version}+{loader_type}-mc{mc_version}"
        loaders = ["neoforge"]
        dependencies = [
            {"project_id": "9s6osm5g", "dependency_type": "required"},  # Cloth Config
        ]

    if not jar_file.exists():
        print(f"[Error] Cannot publish {loader_name.upper()}: JAR file missing ({jar_file})")
        if not is_dry_run:
            sys.exit(1)
        return

    title = build_release_changelog_title(loader_type, mod_version, mc_version)

    payload_data = {
        "name": name,
        "version_number": version_number,
        "changelog": f"{title}\n\n{release_changelog_text}",
        "dependencies": dependencies,
        "game_versions": [mc_version],
        "version_type": release_type,
        "loaders": loaders,
        "featured": True,
        "project_id": project_id,
        "file_parts": [file_key]
    }

    print(f"=== Modrinth Publish Summary ({loader_name.upper()}) ===")
    print(f"Project ID     : {project_id}")
    print(f"Version Name   : {payload_data['name']}")
    print(f"Version Number : {payload_data['version_number']}")
    print(f"Release Type   : {payload_data['version_type']}")
    print(f"Game Versions  : {payload_data['game_versions']}")
    print(f"Loaders        : {payload_data['loaders']}")
    print("Files to Upload:")
    if jar_file.exists():
        print(f"  [{file_key}] : {jar_file.relative_to(root_dir)} ({jar_file.stat().st_size:,} bytes)")
    else:
        print(f"  [{file_key}] : {jar_file.relative_to(root_dir)} (MISSING)")
    print("Dependencies:")
    for dep in dependencies:
        print(f"  - {dep['project_id']} ({dep['dependency_type']})")
    print("\nChangelog Preview:")
    print("-" * 50)
    print(release_changelog_text[:300] + ("..." if len(release_changelog_text) > 300 else ""))
    print("-" * 50)

    if is_dry_run:
        print(f"\n[Dry Run] Verification complete for {loader_name.upper()}. No API request was sent to Modrinth.")
        return

    if not requests:
        print("Error: 'requests' module not found. Please install via `pip install requests`.")
        sys.exit(1)

    print(f"\n[Publishing] Uploading {loader_name.upper()} version to Modrinth API...")
    url = "https://api.modrinth.com/v2/version"
    headers = {
        "Authorization": token,
        "User-Agent": f"datsuns/{project_id}/{mod_version} (me.datsuns@gmail.com)"
    }

    files = {
        "data": (None, json.dumps(payload_data), "application/json"),
        file_key: (jar_file.name, open(jar_file, "rb"), "application/java-archive")
    }

    try:
        response = requests.post(url, headers=headers, files=files)
        if response.status_code in (200, 201):
            res_json = response.json()
            version_id = res_json.get("id", "unknown")
            print(f"\n[Success] {loader_name.upper()} version published successfully to Modrinth!")
            print(f"Version ID: {version_id}")
            print(f"URL: https://modrinth.com/mod/{project_id}/version/{version_id}")
        else:
            print(f"\n[Error] Failed to publish {loader_name.upper()} version (HTTP {response.status_code}):")
            print(response.text)
            sys.exit(1)
    finally:
        for f_obj in files.values():
            if hasattr(f_obj[1], "close"):
                f_obj[1].close()


if __name__ == "__main__":
    main()
