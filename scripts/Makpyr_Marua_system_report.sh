#!/bin/bash
# Makpyr Marua system report: prints a summary and appends it to the log.

STUDENT="Marua Makpyr"
GROUP="IT2-2306"
STUDENT_ID="37578"

# Log path is built from the script location: works from any folder.
PROJECT_DIR="$(cd "$(dirname "$0")/.." && pwd)"
LOG_FILE="$PROJECT_DIR/logs/Makpyr_Marua_system_report.log"
mkdir -p "$(dirname "$LOG_FILE")"
if [ "$(uname -s)" = "Darwin" ]; then
    OS_NAME="$(sw_vers -productName) $(sw_vers -productVersion)"
    CPU="$(sysctl -n machdep.cpu.brand_string), $(sysctl -n hw.ncpu) cores"
    MEM_TOTAL="$(( $(sysctl -n hw.memsize) / 1024 / 1024 / 1024 )) GB"
    PAGE=$(sysctl -n hw.pagesize)
    FREE_PAGES=$(vm_stat | awk '/Pages free|Pages inactive/ {gsub("\\.", "", $NF); s += $NF} END {print s}')
    MEM_FREE="$(( FREE_PAGES * PAGE / 1024 / 1024 )) MB"
else
    OS_NAME="$(. /etc/os-release && echo "$PRETTY_NAME")"
    CPU="$(grep -m1 'model name' /proc/cpuinfo | cut -d: -f2 | xargs), $(nproc) cores"
    MEM_TOTAL="$(free -h | awk '/Mem:/ {print $2}')"
    MEM_FREE="$(free -h | awk '/Mem:/ {print $7}')"
fi

DISK_FREE="$(df -h / | awk 'NR==2 {print $4}')"
{
    echo "========== Makpyr Marua system report =========="
    echo "Student:      $STUDENT ($GROUP, ID $STUDENT_ID)"
    echo "Date:         $(date '+%Y-%m-%d %H:%M:%S')"
    echo "User:         ${USER:-$(whoami)}"
    echo "OS:           $OS_NAME"
    echo "Kernel:       $(uname -sr)"
    echo "CPU:          $CPU"
    echo "Memory:       total $MEM_TOTAL, available $MEM_FREE"
    echo "Disk usage (df -h /):"
    df -h / | sed 's/^/    /'
    echo "Free space:   $DISK_FREE"
    echo "================================================"
} | tee -a "$LOG_FILE"
